-- Network name and password
SSID = "M112-PD"
SSID_PASSWORD = 'aiv4aith2Zie4Aeg'

-- Unique device ID
HUB_ID = "0xDEFEC7EDDA7ABA5E"
RESTAURANT_ID = "test_resto"

APP_HOST = "http://piquemedia.me"
HUB_ENDPOINT = "/hub"
ORDER_ENDPOINT = "/order"

-- configure ESP as a station
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID, SSID_PASSWORD)
wifi.sta.autoconnect(1)

-- pause for connection to take place
tmr.delay(1000000) -- wait 1,000,000 us = 1 second

-- Builds a URL to be used to request a menu from the API
-- customer_id: unique customer identifier, provided by the user's phone
-- restaurant_id: unique restaurant identifier
-- order_id: unique order identifier, provided by the user's phone
function build_menu_get(customer_id, restaurant_id, order_id)
  return  APP_HOST ..
          ORDER_ENDPOINT ..
          "?table_id=" .. HUB_ID ..
          "&restaurant_id=" .. restaurant_id ..
          "&order_id=" .. order_id ..
          "&customer_id=" .. customer_id
end

function get_menu(customer_id, order_id)
  http.get( build_menu_get(customer_id, RESTAURANT_ID, order_id),
            "Content-Type: application/csv\r\n",
            function(code, payload)

              if (code < 0) then
                print("HTTP request failed")
              else
                print(payload)
              end
          end)
end
