#ifndef KEYBOARD_H_
#define KEYBOARD_H_

#include <stdio.h>
#include "../serial/touch.h"
#include "../serial/graphics.h"
#include "../serial/colours.h"
#include "../UIElements/element.h"

void drawKeyboard();
void type(int letter);

#endif /* KEYBOARD_H_ */
