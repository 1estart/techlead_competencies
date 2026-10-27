#!/usr/bin/env bash
set -euo pipefail

# Чтобы Spark не ругался на hostname в WSL
export SPARK_LOCAL_IP=127.0.0.1

# Автопоиск SPARK_HOME
if [[ -z "${SPARK_HOME:-}" ]]; then
  if command -v spark-submit >/dev/null 2>&1; then
    SPARK_HOME=$(dirname "$(dirname "$(readlink -f "$(command -v spark-submit)")")")
  else
    echo "spark-submit not found. Set SPARK_HOME manually." >&2
    exit 1
  fi
fi

# Берём API server из kubeconfig
API_SERVER="$(kubectl config view --minify -o jsonpath='{.clusters[0].cluster.server}')"
API_SERVER="${API_SERVER%/}"

# Если вдруг там без схемы — добавляем https://
if [[ ! "$API_SERVER" =~ ^https?:// ]]; then
  API_SERVER="https://${API_SERVER}"
fi

# ВАЖНО: Spark Kubernetes master должен быть k8s://https://...
MASTER_URI="k8s://${API_SERVER}"

IMAGE="${IMAGE:-spark-demo:1.0}"
MAIN_CLASS="${MAIN_CLASS:-SimpleApp}"
NAMESPACE="${NAMESPACE:-spark-demo}"
SERVICE_ACCOUNT="${SERVICE_ACCOUNT:-spark}"
APP_JAR_IN_IMAGE="${APP_JAR_IN_IMAGE:-/opt/spark/jobs/app.jar}"

echo "SPARK_HOME: $SPARK_HOME"
echo "API_SERVER: $API_SERVER"
echo "MASTER_URI: $MASTER_URI"
echo "IMAGE: $IMAGE"
echo "MAIN_CLASS: $MAIN_CLASS"

exec "$SPARK_HOME/bin/spark-submit" \
  --master "$MASTER_URI" \
  --deploy-mode cluster \
  --name spark-k8s-demo \
  --class "$MAIN_CLASS" \
  --conf "spark.kubernetes.container.image=${IMAGE}" \
  --conf "spark.kubernetes.namespace=${NAMESPACE}" \
  --conf "spark.kubernetes.authenticate.driver.serviceAccountName=${SERVICE_ACCOUNT}" \
  --conf "spark.kubernetes.container.image.pullPolicy=IfNotPresent" \
  --conf "spark.executor.instances=1" \
  --conf "spark.driver.memory=512m" \
  --conf "spark.executor.memory=512m" \
  --conf "spark.sql.shuffle.partitions=2" \
  "local://${APP_JAR_IN_IMAGE}"
