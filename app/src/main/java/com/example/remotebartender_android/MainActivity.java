package com.example.remotebartender_android;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.hivemq.client.annotations.Immutable;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.datatypes.Mqtt5UserProperties;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5SubscribeBuilder;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    //Mqtt variables
    Mqtt5BlockingClient client;
    String host = "broker.mqttdashboard.com";

    //Layout variables
    Button sendButton;
    Button connectButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupElements();



    }


    void setupElements() {
        client = Mqtt5Client.builder().identifier(UUID.randomUUID().toString()).serverHost(host).buildBlocking();
        connectButton = findViewById(R.id.btnConnect);
        sendButton = findViewById(R.id.btnSend);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToHiveMqttBroker();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish("bartender", "Long Island");
            }
        });
    }

    void connectToHiveMqttBroker(){
        Mqtt5ConnAck connAck = client.connect();
        Toast.makeText(this, "Verbunden mit Broker\n" +host , Toast.LENGTH_SHORT).show();
        System.out.println(connAck);
    }

    void publish(String topic, String message){
        Mqtt5PublishResult publishResult = client.publishWith().topic(topic).payload(message.getBytes()).send();
        System.out.println(publishResult);
    }


}
