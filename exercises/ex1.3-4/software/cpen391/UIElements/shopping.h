#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "../UIElements/element.h"
#include "mainmenu.h"

#define MAX 30
#define numItems 6

void drawShopping(void);
void newItem(void);
void addToShoppingList(char *string);
void removeFromShoppingList(int j);
void drawCheckbox(int x, int y);
void drawArrows(void);
void nextPage(void);
void previousPage(void);
