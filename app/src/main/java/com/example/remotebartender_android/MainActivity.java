package com.example.remotebartender_android;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Mqtt variables
    Mqtt5BlockingClient client;
    String host = "broker.mqttdashboard.com";

    //Layout variables
    Button btnSpezi;
    Button btnASchorle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupElements();
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectToHiveMqttBroker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        disconnect();
    }

    void setupElements() {
        //Setup variables and listeners
        client = Mqtt5Client.builder().identifier(UUID.randomUUID().toString()).serverHost(host).buildBlocking();
        btnSpezi = findViewById(R.id.btnSpezi);
        btnASchorle = findViewById(R.id.btnGinTonic);
        btnASchorle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish("bartender","Apfelschorle");
            }
        });
        btnSpezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish("bartender", "Spezi");
            }
        });

        //Connect to the online broker
        connectToHiveMqttBroker();
    }

    void disconnect(){
        client.disconnect();
    }

    void connectToHiveMqttBroker(){
       try{
           Mqtt5ConnAck connAck = client.connect();
           Toast.makeText(this, "Verbunden mit Broker\n" +host , Toast.LENGTH_SHORT).show();
           System.out.println(connAck);
       }catch (Exception e){
           System.out.println(e.toString());
       }




    }

    void publish(String topic, String message){
        Mqtt5PublishResult publishResult = client.publishWith().topic(topic).payload(message.getBytes()).send();
        System.out.println(publishResult);
    }


}
