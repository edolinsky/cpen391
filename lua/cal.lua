-- Network name and password
SSID = "M112-PD"
SSID_PASSWORD = 'aiv4aith2Zie4Aeg'

CALENDAR_ID = "mnk20asbc60qi9bchj809ld3kc@group.calendar.google.com"
APP_HOST = "http://dolins.ky/"
CALENDAR_URL = "calendar"
USER = "erik"
PASSWORD = "cpen"
WEEK = 0
calendar_file = "cal.txt"
-- configure ESP as a station
wifi.setmode(wifi.STATION)
wifi.sta.config(SSID, SSID_PASSWORD)
wifi.sta.autoconnect(1)

-- pause for connection to take place
tmr.delay(1000000) -- wait 1,000,000 us = 1 second

-- Builds a Calendar request using the specified fields
-- cal_id: google calendar ID for this calendar
-- user: application user name
-- password: application user password
-- week: integer, with 0 representing the current Sunday-based week, going
--       forward and back one week with each step
function build_cal_get(cal_id, user, password, week)
  return  APP_HOST ..
          CALENDAR_URL ..
          "?calendar_id=" .. cal_id ..
          "&user=" .. user ..
          "&password=" .. password ..
          "&week=" .. week
end

-- Retrieves a calendar entry from the application server
-- cal_id: google calendar ID for this calendar
-- user: application user name
-- password: application user password
-- week: integer, with 0 representing the current Sunday-based week, going
--       forward and back one week with each step
function get_cal(cal_id, user, password, week)

  -- Resort to default values if we don't have any one argument
  if (cal_id == nil) then
    cal_id = CALENDAR_ID
  end if (user == nil) then
    user = USER
  end if (password == nil) then
    password = password
  end if (week == nil) then
    week = 0
  end

  -- Execute our HTTP request given the arguments
  http.get( build_cal_get(cal_id, user, password, week),
            "Content-Type: application/text\r\n",
            function(code, payload)

      -- Error case: print that we have failed. Do nothing with the existing file
      if (code < 0) then
        print("HTTP request failed")
      else
        -- Clear existing file, and open for writing
        file.remove(calendar_file)
        file.open(calendar_file, "w")

        -- Write payload to file
        file.write(payload)
        collectgarbage()
      end
    end)
end

-- Read contents of file 'filename'
function read_file(filename)
  if file.open(filename, "r") then

    print(file.read())
    file.close()
  end
end

-- Synchronizes the system clock using the default NTP server.
function ntp_sync()

  -- Resolve IP of default NTP server
  net.dns.resolve(nil, function(sk, ip)
    print(NTP_URL, ip)

    -- Error case: We haven't found the IP address of our NTP server
    if (ip == nil) then
      print("DNS for NTP Failed!")
    else

      -- Now that we have the NTP Server's IP address, sync!
      sntp.sync(ip,

        -- Sync is successful. This will set the real-time clock
        -- to the desired value
        function(sec, usec, server, info)
          print('sync', sec, usec, server)
        end,

        -- Sync has failed; do nothing but inform the user
        function(err_code, err_info)
          print('failed!', err_code, err_info)
        end,
        1
      )
    end
  end
  )
end
