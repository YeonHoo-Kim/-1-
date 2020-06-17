#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>

#define USE_SERIAL Serial

WiFiMulti wifiMulti;

const char *ssid = "********";
const char *password = "******"; //password

int trig = 12; 
int echo = 14;  

int flag =0;

void setup()
{
  //Set HC-SR04
  Serial.begin(115200);  
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);
  Serial.print("HC-SR04 IS ready"); 
  
  wifiMulti.addAP(ssid, password);
}

void loop()
{
  //Calculate distance
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  
  int distance = pulseIn(echo, HIGH) * 17 / 1000;      
  Serial.print(distance);    
  Serial.println("cm"); 

  //if car distance is lower than 50cm(for figure)
  if(distance < 50){
    //parked longer then 5s (for figure)
    delay(50000);
    //if the car is still parked
    if((distance < 50) && (flag ==0)){
       //connect wifi
       flag = 1;
       
       if((wifiMulti.run() == WL_CONNECTED)) {

        HTTPClient http;
        
        String dis_str = String(distance);
        String geturl = "http://ec2-34-229-114-134.compute-1.amazonaws.com:8082/hc?hc=1";
        //geturl += dis_str;
        //geturl += "&sequence_number=";
        //geturl += seq_str;
        
        USE_SERIAL.print("[HTTP] begin...\n");
        http.begin(geturl); //HTTP

        USE_SERIAL.print("[HTTP] GET...\n");
        // start connection and send HTTP header
        int httpCode = http.GET();

        // httpCode will be negative on error
        if(httpCode > 0) {
            // HTTP header has been send and Server response header has been handled
            USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

            // file found at server
            if(httpCode == HTTP_CODE_OK) {
                String payload = http.getString();
                USE_SERIAL.println(payload);
            }
        } else {
            USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
        }

        http.end();
      }
    }
    else{
       //connect wifi
       if((wifiMulti.run() == WL_CONNECTED)) {

        HTTPClient http;
        
        String dis_str = String(distance);
        String geturl = "http://ec2-34-229-114-134.compute-1.amazonaws.com:8082/hc?hc=0";
        
        USE_SERIAL.print("[HTTP] begin...\n");
        http.begin(geturl); //HTTP

        USE_SERIAL.print("[HTTP] GET...\n");
        // start connection and send HTTP header
        int httpCode = http.GET();

        // httpCode will be negative on error
        if(httpCode > 0) {
            // HTTP header has been send and Server response header has been handled
            USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

            // file found at server
            if(httpCode == HTTP_CODE_OK) {
                String payload = http.getString();
                USE_SERIAL.println(payload);
            }
        } else {
            USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
        }

        http.end();
      }
    }
  }else{
       //connect wifi
       flag =0;
       
       if((wifiMulti.run() == WL_CONNECTED)) {

        HTTPClient http;
        
        String dis_str = String(distance);
        String geturl = "http://ec2-34-229-114-134.compute-1.amazonaws.com:8082/hc?hc=0";
        
        USE_SERIAL.print("[HTTP] begin...\n");
        http.begin(geturl); //HTTP

        USE_SERIAL.print("[HTTP] GET...\n");
        // start connection and send HTTP header
        int httpCode = http.GET();

        // httpCode will be negative on error
        if(httpCode > 0) {
            // HTTP header has been send and Server response header has been handled
            USE_SERIAL.printf("[HTTP] GET... code: %d\n", httpCode);

            // file found at server
            if(httpCode == HTTP_CODE_OK) {
                String payload = http.getString();
                USE_SERIAL.println(payload);
            }
        } else {
            USE_SERIAL.printf("[HTTP] GET... failed, error: %s\n", http.errorToString(httpCode).c_str());
        }

        http.end();
      }
    }
  
  delay(1000);     
}
