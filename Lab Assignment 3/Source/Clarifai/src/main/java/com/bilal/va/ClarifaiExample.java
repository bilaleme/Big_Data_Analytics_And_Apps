package com.bilal.va;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.ClarifaiRequest;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import okhttp3.OkHttpClient;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.typography.hershey.HersheyFont;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Naga on 22-01-2017.
 */
public class ClarifaiExample {

    static MBFImage image;
    static int x;
    static int y;

    public static void main(String[] args) throws IOException {
        image = ImageUtilities.readMBF(new File("input/maxresdefault.jpg"));
        x = image.getWidth();
        y = image.getHeight();
        DisplayUtilities.displayName(image, "videoFrames");

        final ClarifaiClient client = new ClarifaiBuilder("KKQIegBW9uOl_3vaMSzqq4QCfPNyNBvB7XNBz1vE", "xsY48eiDhhsFo5M7HE3F71ZYkB_tEQmemlWekTgG")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>
        client.getToken();

        client.getDefaultModels().generalModel().predict()
                .withInputs(
                        ClarifaiInput.forImage(ClarifaiImage.of(new File("input/maxresdefault.jpg")))
                )
                .executeAsync(new ClarifaiRequest.OnSuccess<List<ClarifaiOutput<Concept>>>() {
                    public void onClarifaiResponseSuccess(List<ClarifaiOutput<Concept>> clarifaiOutputs) {

                        if (clarifaiOutputs.isEmpty()) {
                            System.out.println("No Predictions");
                        } else {
                            try {

//                                DisplayUtilities.displayName(image, "videoFrames");

                                List<Concept> data = clarifaiOutputs.get(0).data();
                                for (int i = 0; i < data.size(); i++) {
                                    System.out.println(data.get(i).name() + " - " + data.get(i).value());
                                    image.drawText(data.get(i).name(), (int) Math.floor(Math.random() * x), (int) Math.floor(Math.random() * y), HersheyFont.ASTROLOGY, 20, RGBColour.RED);
                                    DisplayUtilities.displayName(image, "videoFrames");
                                }
                                DisplayUtilities.displayName(image, "videoFrames");
                            } catch (Exception ex) {

                            }
                        }
                    }});
//                .executeAsync(ClarifaiRequest.OnSuccess<List<ClarifaiOutput<Concept>>>());
//                .executeSync();
//        List<ClarifaiOutput<Concept>> predictions = (List<ClarifaiOutput<Concept>>) response.get();
//        if (predictions.isEmpty()) {
//            System.out.println("No Predictions");
//        }
//        else{
//            MBFImage image = ImageUtilities.readMBF(new File("input/maxresdefault.jpg"));
//            int x = image.getWidth();
//            int y = image.getHeight();
//
//
//            List<Concept> data = predictions.get(0).data();
//            for (int i = 0; i < data.size(); i++) {
//                System.out.println(data.get(i).name() + " - " + data.get(i).value());
//                image.drawText(data.get(i).name(), (int)Math.floor(Math.random()*x), (int) Math.floor(Math.random()*y), HersheyFont.ASTROLOGY, 20, RGBColour.RED);
//            }
//            DisplayUtilities.displayName(image, "videoFrames");

                }
    }
