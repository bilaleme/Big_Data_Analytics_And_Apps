import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by Naga on 13-03-2017.
 */
@WebServlet(name = "ImageService", urlPatterns = "/ImageService")
public class ImageService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = req.getReader();
        String response="";
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String data = buffer.toString();
        System.out.println(data);
        String output = "";
        JSONObject params = new JSONObject(data);
        JSONObject result = params.getJSONObject("result");
        JSONObject parameters = result.getJSONObject("parameters");
        if (parameters.get("transport").toString().equals("transport")) {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("http://media1.santabanta.com/full1/Air%20Transport/Planes/planes-193a.jpg");
            jsonArray.put("https://static.pexels.com/photos/24353/pexels-photo.jpg");
            jsonArray.put("http://images.r.cruisecritic.com/features/old/westerdam-ship-size.jpg");
            jsonObject.put("data", jsonArray);
            output = jsonObject.toString();
            Data data_ob = Data.getInstance();
            data_ob.setData(output);
            data_ob.setFlag(true);
            JSONObject js = new JSONObject();
            js.put("speech", "Means of transport are displayed");
            js.put("displayText", "transport is displayed");
            js.put("source", "image database");
            response = js.toString();
        }
        else if (parameters.get("transport").toString().equals("planes")) {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("http://media1.santabanta.com/full1/Air%20Transport/Planes/planes-193a.jpg");
            jsonArray.put("http://cdn.timesofisrael.com/uploads/2015/07/Ryanair-plane1.jpg");
            jsonArray.put("https://lumiere-a.akamaihd.net/v1/images/image_eb7b98ae.jpeg");
            jsonArray.put("https://best-wallpaper.net/wallpaper/1920x1080/1107/An-225-Mriya-plane-in-sky_1920x1080.jpg");
            jsonObject.put("data", jsonArray);
            output = jsonObject.toString();
            Data data_ob = Data.getInstance();
            data_ob.setData(output);
            data_ob.setFlag(true);
            JSONObject js = new JSONObject();
            js.put("speech", "planes are displayed");
            js.put("displayText", "planes are displayed");
            js.put("source", "image database");
            response = js.toString();
        }
        else if (parameters.get("transport").toString().equals("cars")){
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("https://static.pexels.com/photos/24353/pexels-photo.jpg");
            jsonArray.put("http://img.autobytel.com/car-reviews/autobytel/11694-good-looking-sports-cars/2016-Ford-Mustang-GT-burnout-red-tire-smoke.jpg");
            jsonArray.put("http://media.caranddriver.com/images/media/51/25-cars-worth-waiting-for-lp-ford-gt-photo-658253-s-original.jpg");
            jsonArray.put("http://blog.caranddriver.com/wp-content/uploads/2017/01/2017-EC-Small-Cars-Honda-Civic-hatchback.jpg");
            jsonObject.put("data", jsonArray);
            output = jsonObject.toString();
            Data data_ob = Data.getInstance();
            data_ob.setData(output);
            data_ob.setFlag(true);
            JSONObject js = new JSONObject();
            js.put("speech", "cars are displayed");
            js.put("displayText", "cars are displayed");
            js.put("source", "image database");
            response = js.toString();
        }
        else if (parameters.get("transport").toString().equals("ships")){
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("http://images.r.cruisecritic.com/features/old/westerdam-ship-size.jpg");
            jsonArray.put("http://i2.cdn.cnn.com/cnnnext/dam/assets/160205192735-01-best-cruise-ships-disney-dream-super-169.jpg");
            jsonArray.put("https://upload.wikimedia.org/wikipedia/commons/9/95/MSMajestyOfTheSeasEdit1.JPG");
            jsonObject.put("data", jsonArray);
            output = jsonObject.toString();
            Data data_ob = Data.getInstance();
            data_ob.setData(output);
            data_ob.setFlag(true);
            JSONObject js = new JSONObject();
            js.put("speech", "ships are displayed");
            js.put("displayText", "ships are displayed");
            js.put("source", "image database");
            response = js.toString();
        }
        else if (parameters.get("null").toString().equals("clear")){
            Data data_ob = Data.getInstance();
            JSONObject js1 = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(" ");
            js1.put("data", jsonArray);
            data_ob.setData(js1.toString());
            data_ob.setFlag(true);
            JSONObject js = new JSONObject();
            js.put("speech", "screen is cleared");
            js.put("displayText", "screen is cleared");
            js.put("source", "image database");
            response = js.toString();
        }
        resp.setHeader("Content-type", "application/json");
        resp.getWriter().write(response);
    }
}
