/*
 * notify.c
 *
 * Created on: March 27, 2017
 *      Author: edolinsky
 */

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "notify.h"
#include "../main.h"
#include "../serial/rs232.h"
#include "../serial/wifi.h"

/**
 * Send a command string to the WiFi chip, causing it to trigger an
 * Android notification to this device's table attendant.
 */
void call_attendant() {
    putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\ncall_attendant()\r\n");
}
