#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "../UIElements/button.h"

void drawKeyboard();
void type(char letter);

#endif
