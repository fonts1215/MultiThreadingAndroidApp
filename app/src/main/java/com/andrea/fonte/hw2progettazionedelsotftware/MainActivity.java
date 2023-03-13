package com.andrea.fonte.hw2progettazionedelsotftware;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public EditText apiUrlComponent;
    public Button addThreadButtonComponent;
    public Button removeThreadButtonComponent;
    public Button addRequestButtonComponent;
    public Button removeRequestButtonComponent;
    public Button runButtonComponeButton;
    public TextView threadCountComponent;
    public TextView requestCountComponent;
    public TextView logViewComponent;
    private Toast toast;

    private String apiUrl = "http://time.jsontest.com";
    private int threadCount = 1;
    private int requestToDo = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Associate component inside the Activity
        apiUrlComponent = findViewById(R.id.apiUri);
        addThreadButtonComponent = findViewById(R.id.addButtonThread);
        removeThreadButtonComponent = findViewById(R.id.removeButtonThread);
        runButtonComponeButton = findViewById(R.id.runButton);
        threadCountComponent = findViewById(R.id.threadCount);
        logViewComponent = findViewById(R.id.logView);
        addRequestButtonComponent = findViewById(R.id.addButtonRequest);
        removeRequestButtonComponent = findViewById(R.id.removeButtonRequest);
        requestCountComponent = findViewById(R.id.requestCount);


        // Initialize the component value
        threadCountComponent.setText(threadCount + "");
        requestCountComponent.setText(requestToDo + "");

        apiUrlComponent.setText(apiUrl);

        // Define action for all of the button
        removeThreadButtonComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(threadCount <= 0)
                    return;

                threadCount--;
                threadCountComponent.setText(threadCount + "");
            }
        });

        addThreadButtonComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(threadCount >= 100)
                    return;

                threadCount++;
                threadCountComponent.setText(threadCount + "");
            }
        });

        removeRequestButtonComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestToDo <= 0)
                    return;

                requestToDo--;
                requestCountComponent.setText(requestToDo + "");
            }
        });

        addRequestButtonComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestToDo >= 100)
                    return;

                requestToDo++;
                requestCountComponent.setText(requestToDo + "");
            }
        });

        runButtonComponeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se non ci sono thread settati verra visualizzato un toast di errore
                if (threadCount == 0) {
                    toast = Toast.makeText(MainActivity.this,
                            "No thread setted",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                if(apiUrl.equals("")) {
                    toast = Toast.makeText(MainActivity.this,
                            "No url setted",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                logViewComponent.setText("");
                long startTime = System.currentTimeMillis();
                ExecutorService executor = Executors.newFixedThreadPool(threadCount);
                for(int i = 0; i < requestToDo; i++){
                    executor.submit(new Runnable() {
                        public void run() {
                            apiCall(Thread.currentThread().getId());
                        }
                    });
                }
                awaitTerminationAfterShutdown(executor);
                toast = Toast.makeText(MainActivity.this,
                        "Finisched in: " + (System.currentTimeMillis() - startTime) + "ms" ,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        // Definisco l'azione per la text field che contiene l'apiurl request
        apiUrlComponent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("api", textView.getText().toString());
                apiUrl = textView.getText().toString();
                return false;
            }
        });
    }

    public void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public String apiCall(long threadId){
        long requestStartTime = System.currentTimeMillis();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.flush();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == 200) {
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertStreamToString(inputStream);
                logViewComponent.setText(
                        logViewComponent.getText()
                                + "\nThread id: " + threadId
                                + " Request time: " + (System.currentTimeMillis() - requestStartTime) + "ms"
                                + " Response: " + response);

                logViewComponent.setMovementMethod(new ScrollingMovementMethod());
                Log.e("MainActivity Response", response);
                return response;
            } else {
                return "Error: " + statusCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }
}

