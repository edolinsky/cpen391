#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "../UIElements/element.h"

#define TEXTAREA 150
#define KEYSIZE 75
#define MARGIN 30
#define MAX_CHARACTERS 120

char* keyboard(void);
void drawUi(void);
void drawKeys(void);
void type(int letter);

#endif /* KEYBOARD_H_ */