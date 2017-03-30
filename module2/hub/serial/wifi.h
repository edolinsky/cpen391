/*
 * wifi.h
 *
 *  Created on: Mar 27, 2017
 *      Author: edolinsky
 */

#ifndef WIFI_H_
#define WIFI_H_

#define WIFI_CONTROL 	(*(volatile unsigned char *) (0x84000210))
#define WIFI_STATUS 	(*(volatile unsigned char *) (0x84000210))
#define WIFI_TXDATA 	(*(volatile unsigned char *) (0x84000212))
#define WIFI_RXDATA     (*(volatile unsigned char *) (0x84000212))
#define WIFI_BAUD       (*(volatile unsigned char *) (0x84000214))

// WiFi functions
void initWiFi(void);
char* wifiListen(char* buf, int maxLen);

#endif /* WIFI_H_ */
