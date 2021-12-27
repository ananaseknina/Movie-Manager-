package com.company;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.StringTokenizer;

public class GUIForm extends JFrame {

    //GUI created with GUI designer, I only added some frame specifications. Adding the main panel was also necessary in IntelliJ

    public JPanel mainPanel;
    public JButton randomButton;
    public JButton trendingButton;
    public JButton seeUpcomingButton;
    public JButton searchByTitleButton;
    public JButton seenButton;
    public JButton wannaSeeButton;
    public JTable Results;
    String iconImageURL = "C:\\Users\\nina\\IdeaProjects\\MovieManager\\programming.png";
    int frameHeight = 500;
    int frameWidth = 400;
    String getRandomApiURL = "https://movies-tvshows-data-imdb.p.rapidapi.com/?type=get-random-movies&page=1";
    String getByTitleURL = "https://movies-tvshows-data-imdb.p.rapidapi.com/?type=get-movies-by-title&title=";
    String getTrendingURL = "https://movies-tvshows-data-imdb.p.rapidapi.com/?type=get-trending-movies&page=1";
    String getUpcomingURL = "https://movies-tvshows-data-imdb.p.rapidapi.com/?type=get-upcoming-movies&page=1";
    String apiHost = "movies-tvshows-data-imdb.p.rapidapi.com";
    String apiKey = "ea24ade45amshf65efc8cee08ec0p1aca2djsn2847445eb31e";

    public GUIForm(String title) {

        //window parameters
        ImageIcon img = new ImageIcon(iconImageURL);
        this.setIconImage(img.getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(title);
        this.add(mainPanel);
        this.setSize(frameWidth, frameHeight);
        this.setResizable(false);
        this.setVisible(true);

        //action listeners

        searchByTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByTitleOperation();
            }
        });

        seeUpcomingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setGUIAfterSeeUpcomingOperation(getUpcomingOperation());
                } catch (UnirestException ex) {
                    ex.printStackTrace();
                }
            }
        });

        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setGUIAfterGetRandomOperation(getRandomOperation());
                } catch (UnirestException ex) {
                    ex.printStackTrace();
                }
            }
        });

        trendingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setGUIAfterGetTrendingOperation(getTrendingOperation());
                } catch (UnirestException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    // methods for action listeners

    public GetRandomResponse getRandomOperation() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(getRandomApiURL)
                .header("x-rapidapi-host", apiHost)
                .header("x-rapidapi-key", apiKey)
                .asJson();
        Gson gson = new GsonBuilder().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        GetRandomResponse resp = gson.fromJson(je,GetRandomResponse.class);

        return resp;

    }

    public void setGUIAfterGetRandomOperation(GetRandomResponse response){

        String[] columns = {"country", "director","genre","imbd_id", "imbd_rating", "language","popularity","rating",
        "release_date", "runtime","stars", "title", "vote count", "year", "youtube trailer"};
        String[][] data = new String[response.movie_results.length][15];

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setSize(1000,500);

        int i = 0;
        for (MovieDetails m : response.movie_results) {
            data[i] = m.getTableValues();
            model.addRow(data[i]);
        }

        JFrame frame = new JFrame();

        frame.setSize(800, 500);
        frame.getContentPane().setLayout(new BorderLayout());
        JScrollPane scroller = new JScrollPane(table);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroller);
        frame.setVisible(true);

    }

    public void newTableAndGuiUpdate(GetSearchByTitleResponse response){

        if(response.movie_results==null){
            ErrorWindow error = new ErrorWindow();
            return;
        }
        String[] columns = {"imdb_id", "title", "year"};
        String[][] data = new String[response.movie_results.length][3];

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        int i = 0;
        for (Movie m : response.movie_results) {
            data[i] = m.getTableValues();
            model.addRow(data[i]);
        }

        JFrame frame = new JFrame();

        frame.setSize(500, 500);
        frame.getContentPane().setLayout(new FlowLayout());
        JScrollPane scroller = new JScrollPane(table);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroller);

        frame.setVisible(true);
    }

    public void searchByTitleOperation() {

        JTextField tf = new JTextField(20);
        JButton ok = new JButton("OK");
        JFrame frame = new JFrame();
        ImageIcon img = new ImageIcon(iconImageURL);
        frame.setIconImage(img.getImage());
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        frame.setSize(400, 100);
        frame.setLayout(layout);
        frame.setTitle("Search by Title");
        frame.setResizable(false);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        frame.add(tf, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        frame.add(ok, gc);
        frame.setVisible(true);

        final String[] title = {""};

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setVisible(false);
                String title = tf.getText();

                //check if title is well formatted and return the correct string - method
                title = prepareString(title);

                try {
                    HttpResponse<JsonNode> response = Unirest.get(getByTitleURL + title)
                            .header("x-rapidapi-host", apiHost)
                            .header("x-rapidapi-key", apiKey)
                            .asJson();


                    Gson responseInJSON = new GsonBuilder().create();
                    JsonParser jp = new JsonParser();
                    JsonElement je = jp.parse(response.getBody().toString());
                    String PrettyJSONString = responseInJSON.toJson(je);
                    Gson gson = new GsonBuilder().create();
                    GetSearchByTitleResponse resp = gson.fromJson(PrettyJSONString, GetSearchByTitleResponse.class);

                    //create JTable with given search by title response
                    newTableAndGuiUpdate(resp);
                } catch (UnirestException ex) {
                    ex.printStackTrace();
                }

            }

        });

    }

    public String prepareString(String userInput){
        String output="";
        StringTokenizer tokenizer = new StringTokenizer(userInput," ");
        while(tokenizer.hasMoreTokens()){
            output+=tokenizer.nextToken()+"%20";
        }
        return output;

    }

    public GetTrendingResponse getTrendingOperation() throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get(getTrendingURL)
                .header("x-rapidapi-host", apiHost)
                .header("x-rapidapi-key", apiKey)
                .asJson();

        Gson gson = new GsonBuilder().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        GetTrendingResponse resp = gson.fromJson(je,GetTrendingResponse.class);

        return resp;

   }

   public void setGUIAfterGetTrendingOperation(GetTrendingResponse response){
       String[] columns = {"imdb_id", "title", "year"};
       String[][] data = new String[response.movie_results.length][3];

       DefaultTableModel model = new DefaultTableModel(columns, 0);
       JTable table = new JTable(model);

       int i = 0;
       for (Movie m : response.movie_results) {
           data[i] = m.getTableValues();
           model.addRow(data[i]);
       }

       JFrame frame = new JFrame();

       frame.setSize(500, 500);
       frame.getContentPane().setLayout(new FlowLayout());
       JScrollPane scroller = new JScrollPane(table);
       scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
       scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
       frame.getContentPane().add(scroller);

       frame.setVisible(true);
   }

    public GetUpcomingResponse getUpcomingOperation() throws UnirestException {

        HttpResponse<JsonNode> response = Unirest.get(getUpcomingURL)
                .header("x-rapidapi-host", apiHost)
                .header("x-rapidapi-key", apiKey)
                .asJson();

        Gson gson = new GsonBuilder().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(response.getBody().toString());
        GetUpcomingResponse resp = gson.fromJson(je, GetUpcomingResponse.class);
        return resp;
    }

    public void setGUIAfterSeeUpcomingOperation(GetUpcomingResponse response) {

        String[] columns = {"imdb_id", "title", "year"};
        String[][] data = new String[response.movie_results.length][3];

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        int i = 0;
        for (Movie m : response.movie_results) {
            data[i] = m.getTableValues();
            model.addRow(data[i]);
        }

        JFrame frame = new JFrame();

        frame.setSize(500, 500);
        frame.getContentPane().setLayout(new FlowLayout());
        JScrollPane scroller = new JScrollPane(table);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.getContentPane().add(scroller);
        frame.setVisible(true);
    }


}


