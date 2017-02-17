#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "../UIElements/element.h"
#include "mainmenu.h"

void drawShopping(void);
void newEntry(void);
void removeFromList(int j);
void addToList(char *string);
void drawCheckbox(int x, int y);
void drawArrows(void);
