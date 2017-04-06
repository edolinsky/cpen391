#include <string.h>
#include "successScreen.h"
#include "serverCalled.h"
#include "main.h"
#include "apps/authenticate.h"

void drawSuccess(void){
	int i;
	char authSuccess[] =  "Resty App Authorized!";
	char nextStepMessage[] = "Orders will appear here after you order on your phone";

	app_context = SUCCESS_CONTEXT;

	for(i = 0; i < 480; i ++){
		//   x1,y1, len, colour
		HLine(0, i, 800, GRAY);
	}

	// Write authorization success message.
	for(i = 0; i < strlen(authSuccess); i++){
		OutGraphicsCharFont2a((400 - strlen(authSuccess)*5)+i*10, 50, WHITE, WHITE, authSuccess[i], 0);
	}

	// Write instruction message.
	for(i = 0; i < strlen(nextStepMessage); i++){
		OutGraphicsCharFont2a((400 - strlen(nextStepMessage)*5)+i*10, 75, WHITE, WHITE, nextStepMessage[i], 0);
	}

	initElements();

	refresh();

	printf("Waiting for order info:\n");

	// Keep listening for order info until it is valid.
	int order_info_valid = FALSE;
	while (!order_info_valid) {
		listen_for_order_info();

		// Order info is valid if both IDs are exactly ID_LENGTH characters long.
		if (strlen(order_id) == ID_LENGTH && strlen(customer_id) == ID_LENGTH) {
			send_auth_ack();
			order_info_valid = TRUE;
		} else {
			send_auth_error();
		}
	}

	printf("Order ID: ");
	printf(order_id);
	printf("\nCustomer ID:");
	printf(customer_id);
	printf("\n");

	// We have the order ID and customer ID, so we can move on to the order page.
	drawOrderPage();
}
