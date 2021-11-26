package com.knoldus
import com.knoldus.search.getFilesTree
import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration
import scala.concurrent.Await
import scala.language.postfixOps


object search {
  def getFilesTree(path: String): Future[List[String]] = Future {
    def getFilesHelper(dir: File, acc: Boolean = true): List[File] = {
      val files = dir.listFiles
      files ++
        files
          .filter(_.isDirectory)
          .filter(_ => acc)
          .flatMap(getFilesHelper(_, acc))
    }.toList
    val list: Seq[File] = getFilesHelper(new File(path))
    list.asInstanceOf[List[String]]
    }
}

object Main extends App {
  val path = "./src"
  val resp = getFilesTree(path)
  Await.result(resp, Duration.Inf)
  resp map (paths => paths map println)
}