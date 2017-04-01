/*
 * bluetooth.h
 *
 *  Created on: Feb 1, 2017
 *      Author: reidoliveira
 */

#ifndef BLUETOOTH_H_
#define BLUETOOTH_H_

#define BT_CONTROL 	(*(volatile unsigned char *) (0x84000220))
#define BT_STATUS 	(*(volatile unsigned char *) (0x84000220))
#define BT_TXDATA 	(*(volatile unsigned char *) (0x84000222))
#define BT_RXDATA 	(*(volatile unsigned char *) (0x84000222))
#define BT_BAUD 	(*(volatile unsigned char *) (0x84000224))

// Bluetooth functions
void initBluetooth(void);
void setBluetoothName(char *name);
void setBluetoothPassword(char *password);
char* bluetoothListen(char* buf, int maxLen);

#endif /* BLUETOOTH_H_ */
