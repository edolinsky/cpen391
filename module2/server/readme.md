# Resty API - Allez

## Login Endpoint
`/login`

Allows a user to log in.
Methods supported: `POST`

## Login POST
_POST http:\/\/piquemedia.me/login_

Request Body:
```json
{
  "user": "test_user@dolins.ky",
  "password": "password"
}
```
The request body includes the following fields:
* `user`: The user's _unique_ email address
* `password`: The user's _hashed_ password

Response on Success:
```json
{
  "affinity": "customer", 
  "id": "test_user", 
  "user": "test_user@dolins.ky"
}
```
If the user exists and the email/password combination is valid, a third field
`affinity` will be included in the response to let the client application know
whether the user is solely a customer, or is also staff of a particular restaurant.

If the user affinity is _"staff"_, meaning that the user works at a restaurant, the 
ID of the restaurant is also returned, as below:
```json
{
  "affinity": "staff", 
  "id": "71a182e218", 
  "restaurant_id": "test_resto", 
  "user": "test_staff@dolins.ky"
}
```

## Signup Endpoint
`/signup`
Methods supported: `POST`

## Signup POST
_POST http:\/\/piquemedia.me/signup_

Just as in the login endpoint, all that is needed in the request body is a 
unique `user` email and hashed `password`, as below:
```json
{
  "user": "test_user@dolins.ky",
  "password": "password"
}
```

However, if the user wants to sign up as staff of a restaurant, also include
the respective `affinity` and `restaurant_id` fields.

```json
{
  "user": "test_staff3@dolins.ky",
  "affinity": "staff",
  "restaurant_id": "test_resto",
  "password": "password"
}
```

Response on success:
```json
{
  "affinity": "staff", 
  "email": "test_staff3@dolins.ky", 
  "id": "4f09c29ff4", 
  "restaurant_id": "test_resto"
}
```

The above shows the response upon successfully creating a new staff user. The
user has been created, and associated with the particular restaurant. The response
upon creating a customer user is shown below:
```json
{
  "affinity": "customer", 
  "email": "test_user2@dolins.ky", 
  "id": "ebe5461790"
}
```

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
```json
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
A list of menu items `items` is returned, containing objects each with the
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
Request body:
```json
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
```json
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
```json
{
  "message": "Hello World!"
}
```


## Teapot Endpoint
`/teapot`
Methods supported: `GET`

Teapot.

Response:
```json
{
  "type": "teapot"
}
```
