-- Network name and password
SSID = "M112-PD"
SSID_PASSWORD = 'aiv4aith2Zie4Aeg'

-- Unique device ID
HUB_ID = "0xDEFEC7EDDA7ABA5E"
DEFAULT_RESTAURANT_ID = ""
RESTAURANT_ID = "test_resto"    -- default. This is later updated from back-end.

-- Server base URL and supported endpoints.
APP_HOST = "http://piquemedia.me"
CALL_SERVER_ENDPOINT = "/call_server"
ORDER_ENDPOINT = "/order"
RESTAURANT_ENDPOINT = "/restaurant"
TIME_ENDPOINT = "/time"

--  Internal file names.
ORDER_FILE = "order.csv"
TIME_FILE = "time.txt"

-- WiFi Message constants.
MSG_START = "````````````````````"
MSG_END = "`"

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

function build_time_get()
    return  APP_HOST ..
            TIME_ENDPOINT
end

-- Deletes the specified file, and then writes the
-- specified content under a new file with the same filename.
function overwrite_file(filename, content)
    file.remove(filename)
    if file.open(filename, "w+") then
        file.write(content)
        file.close(filename)
    end
    collectgarbage()
end

-- Prints the content stored within the file with the specified name.
function read_file(filename)
    if file.open(filename, "r") then
        print(MSG_START .. file.read() .. MSG_END .. "\r\n")
        file.close(filename)
    end
end

-- Retrieves the order data from the API and stores it in a file.
-- Adds a single grave marker to the end of the server's response so that
-- the message can be parsed accurately.
function get_order(customer_id, order_id)

    -- if restaurant ID has not been set, retrieve it from API.
    if (RESTAURANT_ID == DEFAULT_RESTAURANT_ID) then
        get_restaurant_id()
    end

    http.get(
        build_order_get(customer_id, order_id),
        "Content-Type: text/csv\r\n",
        function(code, payload)

            if (code < 0) then
                print("HTTP request failed.")
            else
                overwrite_file(ORDER_FILE, payload)
            end
        end
    )
end

-- Triggers a request to alert an attendant to a table. Nothing interesting
-- needs to be done with the response.
function call_attendant()

    -- if restaurant ID has not been set, retrieve it from API.
    if (RESTAURANT_ID == DEFAULT_RESTAURANT_ID) then
        get_restaurant_id()
    end

    http.post(
        build_call_attendant_post(),
        'Content-Type: application/json\r\n',
        build_call_attendant_body(),
        function(code, payload)
            if (code < 0) then
                print("HTTP request failed.")
            end
        end
    )
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
        end
    )
end

-- Prints the RESTAURANT_ID string.
function read_restaurant_id()
    print(MSG_START .. RESTAURANT_ID .. MSG_END .. "\r\n")
end

-- Prints the HUB_ID string.
function read_table_id()
    print(MSG_START .. HUB_ID .. MSG_END .. "\r\n")
end

-- Retrieve the current unix epoch time from back-end.
function get_time()
    http.get(
        build_time_get(),
        "Content-Type: text/csv\r\n",
        function(code, payload)
            if (code < 0) then
                print("HTTP request failed.")
            else
                overwrite_file(TIME_FILE, payload)
            end
        end
    )
end
