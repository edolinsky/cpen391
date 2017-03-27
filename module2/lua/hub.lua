-- Network name and password
SSID = "M112-PD"
SSID_PASSWORD = 'aiv4aith2Zie4Aeg'

-- Unique device ID
HUB_ID = "0xDEFEC7EDDA7ABA5E"
RESTAURANT_ID = "test_resto"    -- default. This is later updated from back-end.

-- Server base URL and supported endpoints.
APP_HOST = "http://piquemedia.me"
CALL_SERVER_ENDPOINT = "/call_server"
ORDER_ENDPOINT = "/order"
RESTAURANT_ENDPOINT = "/restaurant"

--  Internal file names.
ORDER_FILE = "order.csv"

-- configure ESP as a station
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID, SSID_PASSWORD)
wifi.sta.autoconnect(1)

-- pause for connection to take place
tmr.delay(1000000) -- wait 1,000,000 us = 1 second

-- Builds a URL to be used to request an order from the API
-- customer_id: unique customer identifier, provided by the user's phone
-- restaurant_id: unique restaurant identifier
-- order_id: unique order identifier, provided by the user's phone
function build_order_get(customer_id, order_id)
    return  APP_HOST ..
            ORDER_ENDPOINT ..
            "?table_id=" .. HUB_ID ..
            "&restaurant_id=" .. RESTAURANT_ID ..
            "&order_id=" .. order_id ..
            "&customer_id=" .. customer_id
end

-- Builds a URL to be used to alert an attendant to this hub's table.
function build_call_attendant_post()
    return  APP_HOST ..
            CALL_SERVER_ENDPOINT
end

-- Builds a URL to be used to query for a restaurant ID given this device's
-- unique hub device identifier.
function build_restaurant_get()
    return  APP_HOST ..
            RESTAURANT_ENDPOINT ..
            "?table_id=" .. HUB_ID
end

-- Builds the body of a POST request used to alert an attendant to this hub's
-- table.
function build_call_attendant_body()
    return  '{"restaurant_id": "' .. RESTAURANT_ID .. '",' ..
            ' "table_id": "' .. HUB_ID .. '"}'
end

-- Deletes the specified file, and then writes the
-- specified content under a new file with the same filename.
function overwrite_file(filename, content)
    file.remove(ORDER_FILE)
    if file.open(ORDER_FILE, "w+") then
        file.write(content)
        file.close(ORDER_FILE)
    end
    collectgarbage()
end

-- Prints the content stored within the file with the specified name.
function read_file(filename)
    if file.open(filename, "r") then
        print(file.read())
        file.close(ORDER_FILE)
    end
end

-- Retrieves the order data from the API and stores it in a file.
-- Adds a single grave marker to the end of the server's response so that
-- the message can be parsed accurately.
function get_order(customer_id, order_id)
    http.get(
        build_order_get(customer_id, order_id),
        "Content-Type: text/csv\r\n",
        function(code, payload)

            if (code < 0) then
                print("HTTP request failed.")
            else
                overwrite_file(ORDER_FILE, payload .. "`")
            end
    end)
end

-- Triggers a request to alert an attendant to a table. Nothing interesting
-- needs to be done with the response.
function call_attendant()
    http.post(
        build_call_attendant_post(),
        'Content-Type: application/json\r\n',
        build_call_attendant_body(),
        function(code, payload)
            if (code < 0) then
                print("HTTP request failed.")
            else
                print(code, payload)
            end
    end)
end

-- Retrieves the restaurant ID that this device belongs to, as recorded
-- in the application database.
function get_restaurant_id()
    http.get(
        build_restaurant_get(),
        "Content-Type: text/csv\r\n",
        function(code, payload)
            if (code < 0) then
                print("HTTP request failed.")
            else
                RESTAURANT_ID = payload
            end
    end)
end
