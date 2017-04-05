#include "successScreen.h"
#include "serverCalled.h"
#include "main.h"
#include "apps/authenticate.h"

void drawSuccess(void){
	int i;
	char title[] = "Orders will appear here after you order on your phone";
	char call[] = "Call your server";
	char test[] = "test";

	app_context = SUCCESS_CONTEXT;

	for(i = 0; i < 480; i ++){
		//   x1,y1, len, colour
		HLine(0, i, 800, GRAY);
	}

	// Write page title
	for(i = 0; i < strlen(title); i++){
		OutGraphicsCharFont2a((400 - strlen(title)*5)+i*10, 50, WHITE, WHITE, title[i], 0);
	}

	initElements();

	// Create three buttons
	// --------------------------------   x,   y, wid,  ht, colour
	Element *callButton = createElement(575, 425, 225,  75, DIM_GRAY);
	Element *testButton = createElement(200, 200, 400, 100, DIM_GRAY);

	// Set actions
	setElementAction(callButton, &drawServerCalled);
	setElementAction(testButton, &drawOrderPage);

	// Draw buttons
	addElementToList(callButton);
	addElementToList(testButton);

	refresh();


	// Write back button title
	for(i = 0; i < strlen(call); i++){
		OutGraphicsCharFont2a((775 - strlen(call)*10)+i*10, 450, WHITE, WHITE, call[i], 0);
	}

	// Write test button title
	for(i = 0; i < strlen(test); i++){
		OutGraphicsCharFont2a((400 - strlen(test)*5)+i*10, 250, WHITE, WHITE, test[i], 0);
	}

	printf("Waiting for order info:\n");
	listen_for_order_info();

	printf("Order ID: ");
	printf(order_id);
	printf("\nCustomer ID:");
	printf(customer_id);
	printf("\n");


	drawOrderPage();
}
