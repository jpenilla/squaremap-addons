# signs

Add map markers using in-game signs.

## permissions required

* `squaremap.signs.admin`

## add a marker

1. In your game client, place a sign block in your world.

2. Set its text to your liking.

3. Hold a filled map in your primary hand. You may use any map item; it does not need to correspond to your current location.

4. Right-click the sign.

A new map marker is added immediately. Refresh the squaremap website to see it.

## update a marker

If the marker sign's text changes, the map marker is updated immediately.

## remove a marker

Either

* Destroy the marker sign, removing it from the world; OR

* Hold a filled map in your primary hand, and left-click the sign.

The map marker is removed immediately.

## tips and tricks

* Simple/safe HTML is permitted in signs

* Add your own icon images to the `customicons/` directory. If the first line of text on the front side of the sign matches the `icon.custom.regexp`, the matched portion will be interpreted as an icon filename.

  The default regexp matches text like:

  ```
  ![cool_icon]
  ```

  and uses the image file `customicons/cool_icon.png` as the icon.
