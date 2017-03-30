/*
 * notify.c
 *
 * Created on: March 27, 2017
 *      Author: edoinsky
 */

#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include "notify.h"
#include "../main.h"
#include "../serial/rs232.h"
#include "../serial/wifi.h"

void call_attendant() {
    putString(&WIFI_STATUS, &WIFI_TXDATA, "\r\ncall_attendant()\r\n");
}
