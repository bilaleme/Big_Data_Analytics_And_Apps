package com.bilal.speech

import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.rdd.RDD
import scala.collection.mutable.ListBuffer
import breeze.linalg.split

object SpeechAnalysis {

  def nCalc(nArr: RDD[String], word: String): (String, Int) = {
    nArr.map { nW =>
      if (nW.equals(word)) {
        (word, 1)
      } else (word, 0)
    }.reduceByKey(_ + _).collect()(0)

  }

  def main(args: Array[String]) {
    System.setProperty("hadoop.home.dir", "E:\\hadoop")

    val sparkConfig = new SparkConf().setAppName("SpeechAnalysis").setMaster("local[1]")

    val sparkContext = new SparkContext(sparkConfig)

    val ninput = sparkContext.textFile("speechx")
    val yinput = sparkContext.textFile("speech")

    //    val nWords = sparkContext.textFile("nWords")

    val yWords = sparkContext.textFile("yWords").first().toString().split(",")
    val nWords = sparkContext.textFile("nWords").first().toString().split(",")

    val youtput = yinput.flatMap { line =>
      line.split(" ")
    }.map { word =>
      {
        var ycount = 0;
        
        yWords.foreach { nW =>
          if (word.toLowerCase().contains(nW.toLowerCase())) {
            ycount = ycount + 1;
          }
        };
        ("positive",ycount);
      }
    }.reduceByKey(_ + _).collect().foreach(x => println(x));
    
    val noutput = ninput.flatMap { line =>
      line.split(" ")
    }.map { word =>
      {
        var ncount = 0;
        
        nWords.foreach { nW =>
          if (word.toLowerCase().contains(nW.toLowerCase())) {
            ncount = ncount + 1;
          }
        };
        ("negative",ncount);
      }
    }.reduceByKey(_ + _).collect().foreach(x => println(x));

  }
}