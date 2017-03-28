/*
 * notify.c
 *
 * Created on: March 27, 2017
 *      Author: edoinsky
 */

#include "notify.h"

void call_attendant() {
    putString($WIFI_STATUS, &WIFI_TXDATA, "call_attendant()\n\n");
}
