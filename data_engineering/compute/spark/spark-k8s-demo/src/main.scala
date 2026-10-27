//> using scala 2.12.18
//> using jvm 11
//> using dep "org.apache.spark::spark-core:3.5.1"
//> using dep "org.apache.spark::spark-sql:3.5.1"

import org.apache.spark.sql.SparkSession

object SimpleApp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .appName("Simple Spark K8s Demo")
      .getOrCreate()

    import spark.implicits._

    val data = Seq(
      "Apache Spark is awesome",
      "Kubernetes makes deployment easy",
      "Scala and Spark work great together",
      "Data engineering is fun",
      "Big data processing at scale"
    )

    val df = data.toDF("line")
    df.cache()

    val numAs = df.filter($"line".contains("a")).count()
    val numBs = df.filter($"line".contains("b")).count()

    println("=== Results ===")
    println(s"Lines with 'a': $numAs")
    println(s"Lines with 'b': $numBs")
    println("===============")

    spark.stop()
  }
}
