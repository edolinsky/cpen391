/*
 * bluetooth.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef BLUETOOTH_H_
#define BLUETOOTH_H_

#define WIFI_CONTROL 	(*(volatile unsigned char *) (0x84000220))
#define WIFI_STATUS 	(*(volatile unsigned char *) (0x84000220))
#define WIFI_TXDATA 	(*(volatile unsigned char *) (0x84000222))
#define WIFI_RXDATA 	(*(volatile unsigned char *) (0x84000222))
#define WIFI_BAUD 	(*(volatile unsigned char *) (0x84000224))

// WiFi functions
void initWiFi(void);
void setWiFiName(char *name);
void setWiFiPassword(char *password);
void wifiListen();

#endif /* BLUETOOTH_H_ */
