# Resty API - Allez

## Login Endpoint
`/login`

Allows a user to log in.
Methods supported: `POST`

## Login POST

## Signup Endpoint
`/signup`
Methods supported: `POST`

## Signup POST

## Menu Endpoint
`/menu`
Methods supported: `GET`

### Menu GET
_GET http:\/\/piquemedia.me/menu?restaurant_id=<id>&type=appetizer_

Parameters:
* `restaurant_id`: unique ID of the restaurant to be used to retrieve
* `type`: **OPTIONAL.** menu item type enumeration. If specified, response will
contain items only of that type. If not specified, all menu items will be returned.
One of:
  * _"appetizer"_
  * _"drink"_
  * _"alcoholic"_
  * _"entree"_
  * _"dessert"_
  * _"merchandise"_

Response on Success:
```
{
  "items": [
    {
      "description": "Every day is cheat day.",
      "id": "GgaStDohcb",
      "name": "Rocky Road Ice Cream Cake",
      "price": 8.0,
      "type": "dessert"
    },
    {
      "description": "Worth fighting over.",
      "id": "h5jsHqrsJe",
      "name": "Thai Chicken Bites",
      "price": 13.0,
      "type": "appetizer"
    },
    {
      "description": "All the calories in the world.",
      "id": "iKAP0hSHjB",
      "name": "Hamburger",
      "price": 13.0,
      "type": "entree"
    }
  ],
  "restaurant_id": "test_resto"
}
```
A list of menu items `items` has been added, containing objects each with the
following properties:
* `id`: unique ID of the item on the menu
* `name`: String denoting the name of the item
* `description`: String description of the item
* `price`: Price of the item, with one or two digits post-decimal
* `type`: enumerated menu type of the item

## Order Endpoint
`/order`
Methods supported: `POST`

### Order POST
Expected body:
```
{
  "customer_id": "test_user",
  "restaurant_id": "test_resto",
  "table_id": "test_table",
  "items": [
    {"menu_id": "2rs7U6patW", "customer_name": "Erik"},
    {"menu_id": "HMw4vmcmqy", "customer_name": "Erik"},
    {"menu_id": "HMw4vmcmqy", "customer_name": "Erik"}
    ]
}
```
* `customer_id`: unique user ID of the customer placing order.
* `restaurant_id`: unique restaurant ID of the restaurant.
* `table_id`: unique table (device) HUB ID.
* `items`: list of JSON objects, each containing:
  * `menu_id`: ID of the item to be ordered.
  * `customer_name`: identifying string corresponding to the name of the
  person who ordered the item

Response on Success:
```
{
  "customer_id": "test_user",
  "items": [
    {
      "customer_name": "Erik",
      "id": "3838e0e86c",
      "menu_id": "2rs7U6patW",
      "status": "placed"
    },
    {
      "customer_name": "Erik",
      "id": "c121cbede3",
      "menu_id": "HMw4vmcmqy",
      "status": "placed"
    },
    {
      "customer_name": "Erik",
      "id": "54809225cb",
      "menu_id": "HMw4vmcmqy",
      "status": "placed"
    }
  ],
  "order_id": "3be0d4a448",
  "restaurant_id": "test_resto",
  "table_id": "test_table"
}
```

The response is a similar object, but with the following changes:
* `order_id`: unique ID corresponding to this order. Keep track of this to track
of or modify the order.
* Each item has the following fields added:
  * `id`: unique ID of the order item
  * `status`: string denoting status of the item ordered, which will be set to
  _"placed"_, the first order item state.

## Hello Endpoint
`/hello`
Methods supported: `GET`

For testing: responds with a simple message.

Response:
```
{
  "message": "Hello World!"
}
```


## Teapot Endpoint
`/teapot`
Methods supported: `GET`

Teapot.

Response:
```
{
  "type": "teapot"
}
```
