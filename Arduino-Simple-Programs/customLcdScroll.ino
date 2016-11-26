/** yotkaz
 *
 *  custom LCD scroll
 *  Arduino UNO + LCD 16x2 (RGB or not) */

#include <LiquidCrystal.h>

#define P_RS 8
#define P_ENABLE 7
#define P_D4 5
#define P_D5 4
#define P_D6 3
#define P_D7 2

/* rgb only */
#define RED_PIN 9
#define GREEN_PIN 10
#define BLUE_PIN 11

/* longer text example
const String MESSAGE                  = "Global variables use 98 bytes (4%) of dynamic memory, leaving 1,950 bytes for local variables. Maximum is 2,048 bytes.  ###";
*/
const String MESSAGE                  = "Hello World!";
const long   DELAY_TIME               = 500;
const int    LCD_SIZE[]               = {16, 2};

/* AUTO_SPACE_SIZE = true:
 * if text is shorter than x*y display positions, then output is like:
 * 
 * xt______________
 * _________some_te
 * 
 * if text is longer, then output is like:
 * 
 * ext_This_is_some_
 * longer_and_longer
 * 
 * AUTO_SPACE_SIZE = false:
 * 
 * space between repeats are controlled by SPACE_SIZE, e.g.:
 * 
 * ld___Hello_World
 * ___Hello_World__
*/
const bool   AUTO_SPACE_SIZE          = true;
const char   SPACE_CHAR               = ' ';
const int    SPACE_SIZE               = 2;

/*  lines with scroll text
    {0}             - only first line
    {1}             - only second line
    {0, 1}          - first and second line
    {0, 1, 2, 5, x} - selected lines (larger displays)
*/
const int    TEXT_LINES[]             = {0, 1};

/* rgb only */
const bool    RGB                     = true;
const bool    INVERT_COLORS           = true;
const int     BRIGHTNESS              = 255;
const int     START_COLOR[]           = {255, 200, 20};

LiquidCrystal lcd(P_RS, P_ENABLE, P_D4, P_D5, P_D6, P_D7);

int allPositions = 0;
int lines = 0;
int positionX = LCD_SIZE[0];
int positionY = 0;
int seqStart = 0;
int seqEnd = 0;
bool tooLong = false;
String message = MESSAGE;

void setup() {
  lcd.begin(LCD_SIZE[0], LCD_SIZE[1]);
  if (RGB) {
    pinMode(RED_PIN, OUTPUT);
    pinMode(GREEN_PIN, OUTPUT);
    pinMode(BLUE_PIN, OUTPUT);
    setBacklight(START_COLOR[0], START_COLOR[1], START_COLOR[2]);
  }
  lines = sizeof(TEXT_LINES) / sizeof(int);
  allPositions = lines * LCD_SIZE[0];
  positionY = lines - 1;
  if (AUTO_SPACE_SIZE) {
    if (message.length() + 1 > allPositions) {
      message += SPACE_CHAR;
    }
  } else {
    while (message.length() < allPositions) {
      for (int i = 0; i < SPACE_SIZE; i++) {
        message = String(message + SPACE_CHAR);
      }
      message = String(message + MESSAGE);  
    }
    for (int i = 0; i < SPACE_SIZE; i++) {
        message = String(message + SPACE_CHAR);
    }
  }
  if (message.length() > allPositions) {
    tooLong = true;
    seqEnd = allPositions;
  }
  for (int i = 0; i < allPositions; i++) {
    moveLeft(false);
  }
}

void loop() {
  moveLeft(true);
}

/* rolling is presenting not displayed text on the beginning of first lines, e.g.:
 * xt______________
 * _________some_te
*/
void moveLeft(bool roll) {
  lcd.clear();
  lcd.noDisplay();
  String thisMessage = message;
  int mLength = message.length();
  if (tooLong && roll) {
    positionX = 0;
    positionY = 0;
    if (seqEnd >= seqStart) {
      thisMessage = thisMessage.substring(seqStart, seqEnd);
    } else {
      thisMessage = String(thisMessage.substring(seqStart, mLength)
                           + thisMessage.substring(0, seqStart + allPositions - mLength));
    }

    seqStart = (seqStart == mLength) ? 0 : seqStart + 1;
    seqEnd = (seqEnd == mLength) ? 0 : seqEnd + 1;
  }
  if (!roll) {
    int toPrint = allPositions - (positionX + positionY * LCD_SIZE[0]);
    toPrint = (toPrint > thisMessage.length()) ? thisMessage.length() : toPrint;
    thisMessage = thisMessage.substring(0, toPrint);
  }
  int left = thisMessage.length();
  int printed = 0;
  bool started = false;
  for (int i = 0;  i < lines; i++) {
    if (left > 0) {
      int startPosition = 0;
      if (i == positionY) {
        started = true;
        startPosition = positionX;
        lcd.setCursor(startPosition, TEXT_LINES[i]);
      } else {
        if (started) {
          lcd.setCursor(startPosition, TEXT_LINES[i]);
        } else {
          continue;
        }
      }
      int nowPrinted = LCD_SIZE[0] - startPosition;
      left = left - nowPrinted;
      lcd.print(thisMessage.substring(printed,
                                      (left > 0) ? thisMessage.length() - left : thisMessage.length()));
      printed += nowPrinted;
    }
  }
  if (roll) {
    for (int i = 0;  i < lines; i++) {
      if (left > 0) {
        lcd.setCursor(0, TEXT_LINES[i]);
        int nowPrinted = LCD_SIZE[0];
        left = left - nowPrinted;
        lcd.print(thisMessage.substring(printed,
                                        (left > 0) ? thisMessage.length() - left : thisMessage.length()));
        printed += nowPrinted;
      }
    }
  }
  if (!tooLong || !roll) {
    if (positionX == 0) {
      positionX = LCD_SIZE[0] - 1;
      positionY = (positionY == 0) ? lines - 1 : positionY - 1;
    } else {
      positionX--;
    }
  }
  lcd.display();
  delay(DELAY_TIME);
}

/* rgb only */
void setBacklight(int r, int g, int b) {
  /* map to brightness */
  r = map(r, 0, 255, 0, BRIGHTNESS);
  g = map(g, 0, 255, 0, BRIGHTNESS);
  b = map(b, 0, 255, 0, BRIGHTNESS);

  /* invert colors*/
  if (INVERT_COLORS) {
    r = map(r, 0, 255, 255, 0);
    g = map(g, 0, 255, 255, 0);
    b = map(b, 0, 255, 255, 0);
  }

  analogWrite(RED_PIN, r);
  analogWrite(GREEN_PIN, g);
  analogWrite(BLUE_PIN, b);
}
