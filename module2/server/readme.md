# Resty API - Allez

## Login Endpoint
`/login`

Methods supported: `POST`

Allows a user to log in.

## Login POST
_POST http:\//piquemedia.me/login_

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
_POST http:\/\/piquemedia.me\/signup_

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

Where `affinity` can be one of `staff` or `staff_only`. Signing up as `staff_only` will give them
access to just the staff side of the app, while `staff` will give them both customer and staff
privileges.

Response on success:
```json
{
  "affinity": "customer", 
  "email": "test_user2@dolins.ky", 
  "id": "ebe5461790"
}
```
The above shows the response upon successfully creating a new customer user. The response
upon creating a staff user is shown below:


```json
{
  "affinity": "staff", 
  "email": "test_staff3@dolins.ky", 
  "id": "4f09c29ff4", 
  "restaurant_id": "test_resto"
}
```

The user has been created, and associated with the particular restaurant.

## Menu Endpoint
`/menu`

Methods supported: `GET, POST`

### Menu GET
_GET http:\/\/piquemedia.me/menu?restaurant\_id=<id>&type=appetizer_

A Menu GET request is a query for a particular restaurant's menu items.

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

### Menu POST
_POST http:\/\/piquemedia.me/menu_

This endpoint is used to create new menu items under an existing restaurant.

Request body:
```json
{"restaurant_id": "f2e1027e07",
 "items": [
   {
     "name": "Crackalicious Brussel Sprouts",
     "type": "appetizer",
     "description": "Crispy brussel sprouts with black pepper, prosciutto, parmesan and lemon.",
     "price": 7.95
   },
   {
     "name": "Crunchy Free Range Chicken Karage",
     "type": "appetizer",
     "description": "Crunchy bites of free range chicken w/ garlic chips, ponzu mayo and lime pepper dipping sauce.",
     "price": 9.95
   },
   {
     "name": "Pho Nachos",
     "type": "appetizer",
     "description": "The Koerner's trademark, patent pending signature dish. Braised beef, bean sprouts, mozzarella, lime, cilantro, Hoisin and Sriracha.",
     "price": 17.95
   }
 ]
}
```
Where the parameters are similar to those detailed in the _Menu GET_ section above.

Response on Success:
```json
{
  "items": [
    {
      "description": "Crispy brussel sprouts with black pepper, prosciutto, parmesan and lemon.", 
      "id": "a51d4f40d4", 
      "name": "Crackalicious Brussel Sprouts", 
      "price": 7.95, 
      "type": "appetizer"
    }, 
    {
      "description": "Crunchy bites of free range chicken w/ garlic chips, ponzu mayo and lime pepper dipping sauce.", 
      "id": "fdc5d80502", 
      "name": "Crunchy Free Range Chicken Karage", 
      "price": 9.95, 
      "type": "appetizer"
    }, 
    {
      "description": "The Koerner's trademark, patent pending signature dish. Braised beef, bean sprouts, mozzarella, lime, cilantro, Hoisin and Sriracha.", 
      "id": "5963f01899", 
      "name": "Pho Nachos", 
      "price": 17.95, 
      "type": "appetizer"
    }
  ], 
  "restaurant_id": "f2e1027e07"
}
```
Upon success, you'll see that the same object is returned, but identification numbers have
been assigned to each of the menu items.

## Order Endpoint
`/order`

Methods supported: `GET, POST`

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


### Order GET
_GET http:\/\/piquemedia.me\/order?order\_id=26a00ff96d&customer\_id=test\_user&restaurant\_id=test\_resto&table\_id=test\_table_

Retrieves information about the specified order. `order_id`, `table_id`, `customer_id`, and `restaurant_id`
are required fields.

Response on success:
```json
{
  "items": [
    {
      "customer_name": "Erik", 
      "description": "Yeah, we know you don't really like beer.", 
      "id": "179a6f7f56", 
      "menu_id": "HMw4vmcmqy", 
      "name": "Corona", 
      "price": 6.0, 
      "status": "placed", 
      "type": "alcoholic"
    }, 
    {
      "customer_name": "Erik", 
      "description": "Yeah, we know you don't really like beer.", 
      "id": "937db81c0f", 
      "menu_id": "HMw4vmcmqy", 
      "name": "Corona", 
      "price": 6.0, 
      "status": "placed", 
      "type": "alcoholic"
    }, 
    {
      "customer_name": "Erik", 
      "description": "The least healthy of the healthy options.", 
      "id": "b5e1d81c47", 
      "menu_id": "2rs7U6patW", 
      "name": "Caesar Salad", 
      "price": 99.5, 
      "status": "placed", 
      "type": "appetizer"
    }
  ], 
  "order_id": "26a00ff96d", 
  "restaurant_id": "test_resto"
}
```

The response echoes the order and restaurant IDs, and includes a list of objects, each containing:
* `customer_name`: the name of the item recipent
* `description`: a description of the item ordered
* `id`: the unique item order ID
* `menu_id`: the ID corresponding to the item in the restaurant's menu
* `name`: the name of the item
* `price`: the price of the item
* `status`: the status of the ordered item
* `type`: the menu item type of the ordered item

If `"Content-Type": "text/csv"` is set, the response will be in CSV format.

Response on Success:
```
Corona,Erik,placed
Corona,Erik,placed
Caesar Salad,Erik,cancelled
```
Note that the item name, name of consumer, and status are the only three fields provided
when using this response format.

## Orders Endpoint
`/orders`

Methods supported: `GET, PATCH`

This endpoint handles viewing orders and editing status of orders as managed by a restaurant.

### Orders
_http:\//piquemedia.me/orders?restaurant\_id=test\_resto&query=open_

Retrieves orders as specified by query parameters.

Parameters:
* `restaurant_id`: Unique ID of restaurant
* `query`: string specifying query to be made:
    * `open`: retrieve all orders that are not served, complete, or cancelled
    
Response on Success:
```json
{
  "orders": [
    {
      "customer_name": "Erik", 
      "id": "179a6f7f56", 
      "menu_id": "HMw4vmcmqy", 
      "name": "Corona", 
      "status": "placed", 
      "table_id": "test_table"
    }, 
    {
      "customer_name": "Erik", 
      "id": "3838e0e86c", 
      "menu_id": "2rs7U6patW", 
      "name": "Caesar Salad", 
      "status": "prep", 
      "table_id": "test_table"
    }
  ], 
  "restaurant_id": "test_resto"
}
```
Parameters:
* `orders`: contains a list of order objects, each with the following fields:
    * `customer_name`: name of customer to receive order item
    * `id`: unique ID of the order item
    * `name`: name of the item
    * `status`: order status
    * `table_id`: unique ID of hub at table in restaurant

### Orders PATCH
_PATCH http:\/\/piquemedia.me\/orders_

Request Body:
```json
{
  "restaurant_id": "test_resto",
  "items": [ 
    {"id": "3838e0e86c", "status": "prep"},
    {"id": "b5e1d81c47", "status": "ready"},
    ]
}
```
Requests contain the following fields:
* `restaurant_id`: the unique ID of the restaurant.
* `items`: a list, containing objects with the following fields:
    * `id`: the unique ID of the order item.
    * `status`: the new status of the item, which can be one of the following:
        * `placed`
        * `prep`
        * `ready`
        * `served`
        * `complete`
        * `cancelled`

## Restaurant Endpoint
`/restaurant`

Methods supported: `GET, POST`

This endpoint allows for simple creation of new restaurants as well as retrieval
of restaurant-related information. It should not be public-facing, and thus should 
not be included in the user application.

### Restaurant GET
_GET http:\/\/piquemedia.me\/restaurant?table\_id=test\_table_

Intended for use by hub devices, this allows a hub to retrieve the corresponding 
restaurant ID given its unique hub ID. Both JSON and CSV responses are supported, 
based on the requested mime type.

JSON response:
```json
{
  "restaurant_id": "test_resto", 
  "table_id": "test_table"
}
```

CSV response:
```
test_resto
```

### Restaurant POST
_POST http:\/\/piquemedia.me\/restaurant_

Request body:
```json
{"name": "Koerner's Pub"}
```
All that is needed here is the name of the restaurant. This will likely be fleshed
out in the future, but we do not store more than the name at this point.

Response on success:
```json
{
"message": "Restaurant Created.",
"name": "Koerner's Pub",
"restaurant_id": "f2e1027e07"
}
```
A restaurant ID has been created and is associated with this restaurant. A message
is also included, but is unimportant.

## Call Server Endpoint
`/call_server`

Methods supported: `POST`

This endpoint allows hub devices to trigger notifications to staff users.

### Call Server POST
_POST http:\/\/piquemedia.me\/call\_server_

Request body:
```json
{
  "restaurant_id": "test_resto",
  "table_id": "0xDEFEC7EDDA7ABA5E"
}
```

Response on Success:
```json
{
  "message": "Notification Successful.", 
  "restaurant_id": "test_resto", 
  "table_id": "0xDEFEC7EDDA7ABA5E"
}
```

## Server Hub Map Endpoint
`/server_hub_map`

Methods supported: `GET, POST, DELETE`

This endpoint allows staff users to manage the assignment of waitstaff to individual tables.

### Server Hub Map GET
_GET http:\/\/piquemedia.me\/server\_hub\_map?table\_id=test\_table_

This request supplies the set of all current mappings for the specified restaurant.

### Server Hub Map POST
_POST http:\/\/piquemedia.me\/server\_hub\_map_

This request updates the restaurant's mappings to reflect those that are specified.


Request body:
```json
{
  "restaurant_id": "test_resto",
  "mappings": [ 
    {
      "attendant_id": "71a182e218",
      "table_id": "0xDEFEC7EDDA7ABA5E"
    }
  ]
}
```

The following fields are included:
* `restaurant_id`: the unique ID of the restaurant.
* `mappings`: a list, containing objects with the following fields:
    * `attendant_id`: user ID of the waitstaff user to be mapped
    * `table_id`: hub device ID of the device to be mapped

Response on Success:
```json
{
  "mappings": [
    {
      "attendant_id": "71a182e218", 
      "table_id": "0xDEFEC7EDDA7ABA5E"
    }
  ], 
  "restaurant_id": "test_resto"
}
```

### Server Hub Map DELETE
_DELETE http:\/\/piquemedia.me\/server\_hub\_map?attendant\_id=71a182e218&restaurant\_id=test\_resto_

This request removes the specified user from all hub map entries for the specified restaurant.

Response on Success:
```json
{
  "message": "Removed user from table mappings."
}
```

## Update FCM ID Endpoint
`/update_fcm_id`

Methods supported: `POST`

This endpoint allows mobile devices to automatically update a user's Firebase Cloud Messaging ID.

### Update FCM ID POST
_POST http:\/\/piquemedia.me\/update\_fcm\_id

```json
{
  "user": "test_user@dolins.ky",
  "android_reg_id": "Attack of the Clones"
}
```

The following fields must be specified:
* `user`: Unique user email
* `android_reg_id`: Newly generated FCM ID

Response on success:
```json
{
  "android_reg_id": "Attack of the Clones", 
  "message": "FCM ID Update successful.", 
  "user": "test_user@dolins.ky"
}
```

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
