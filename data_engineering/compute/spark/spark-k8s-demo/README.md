# Spark on Minikube Demo

Минимальный демо-проект: Scala-приложение → JAR → Docker-образ → Spark Driver/Executor Pods в Kubernetes.

## Структура

```text
.
├── src/main.scala       # Spark-приложение на Scala
├── Dockerfile           # образ со Spark и app.jar
├── rbac.yaml            # namespace + права для Spark Driver
├── submit.sh            # локальный spark-submit → Kubernetes
├── .dockerignore
└── .gitignore
```

## 1. Собрать JAR

```bash
scala-cli package src/main.scala --assembly --spark -o app.jar
```

`--spark` значит, что Spark не кладется внутрь JAR — он будет в Docker-образе.

Проверка:

```bash
jar tf app.jar | grep SimpleApp
```

## 2. Собрать образ в Minikube

```bash
eval "$(minikube docker-env)"
docker build -t spark-demo:1.0 .
```

Проверка:

```bash
docker images | grep spark-demo
```

## 3. Создать Kubernetes-ресурсы

```bash
kubectl apply -f rbac.yaml
```

Создаются:

- namespace `spark-demo`
- ServiceAccount `spark`
- Role / RoleBinding для создания Driver и Executor Pod’ов

## 4. Запустить джобу

```bash
chmod +x submit.sh
./submit.sh
```

В другом терминале:

```bash
kubectl get pods -n spark-demo -w
```

## 5. Посмотреть логи

```bash
kubectl logs -n spark-demo -l spark-role=driver --tail=300
```

Follow:

```bash
kubectl logs -n spark-demo -l spark-role=driver -f
```

## 6. Остановить / почистить

Удалить только поды:

```bash
kubectl delete pods -n spark-demo --all --ignore-not-found
```

Удалить всё демо:

```bash
kubectl delete namespace spark-demo --ignore-not-found
```

Удалить образ из Minikube:

```bash
eval "$(minikube docker-env)"
docker rmi spark-demo:1.0 --force
eval "$(minikube docker-env -u)"
```

Остановить Minikube:

```bash
minikube stop
```

Полностью удалить Minikube:

```bash
minikube delete
```

## Жизненный цикл

```text
src/main.scala
    ↓
app.jar
    ↓
Docker image: spark-demo:1.0
    ↓
namespace + RBAC
    ↓
spark-submit
    ↓
Driver Pod
    ↓
Executor Pods
    ↓
logs
    ↓
cleanup
```

## Важно

- JAR должен быть валидным: `file app.jar`
- Spark в `submit.sh` и в образе должны быть одной версии
- Scala-версии приложения и Spark должны совпадать
- `submit.sh` должен использовать master вида:

```bash
k8s://https://<api-server>
```