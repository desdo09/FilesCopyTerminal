package Classes.DB;


import Classes.Entities.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SM David on 21/09/2018.
 */
public class Api {
    private String url;

    public Api(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Product> getProducts() {

        try {

            //Making a request
            URL url = new URL(this.url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");


            int status = con.getResponseCode();

            if (status != 200) {
                System.out.println("Error: " + status);
                return null;
            }
            con.setConnectTimeout(10);
            con.setReadTimeout(10);
            //Getting response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Close request
            in.close();
            con.disconnect();


            //Getting json parser
            HashMap<String, String> ans = new HashMap<>();
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(content.toString());

            //Converting each product from json to object
            JSONObject pr;
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < arr.size(); i++) {
                pr = (JSONObject) parser.parse(arr.get(i).toString());
                products.add(new Product(
                        Long.parseLong(pr.get("ProductID").toString()),
                        pr.get("ProductName").toString(),
                        pr.get("BARCODE").toString(),
                        pr.get("ProductCategory").toString(),
                        Long.parseLong(pr.get("ProductCategoryID").toString()),
                        Double.parseDouble(pr.get("SalePrice").toString()),
                        pr.get("SupplierCode").toString()
                ));
            }

            return products;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
