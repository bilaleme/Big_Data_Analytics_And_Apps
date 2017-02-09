package com.bilal.va;

import javafx.animation.KeyFrame;
import org.openimaj.image.MBFImage;
import org.openimaj.video.Video;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class VideoAnnotation {


    public static void main(String[] args){

//        KeyFrameDetection.Frames("input/sample1.mkv"); //get total frames
//        KeyFrameDetection.MainFrames(); // fetch key frames from set of all frames
        try {
            ImageAnnotation.ImageAnnotate(); // fetch the annotation using clarifai api
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
