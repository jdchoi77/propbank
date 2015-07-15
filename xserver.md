# How to run tools from Windows machines #

### 1. Download, install, and run Xming ###

  * Download Xming from the following link: [download](http://downloads.sourceforge.net/sourceforge/xming/Xming-6-9-0-31-setup.exe).
  * Click **Xming-6-9-0-31-setup.exe** and install Xming by using the default settings.
  * Run Xming by clicking **|Xming - Xming|** on the start menu.

### 2. Download and run Putty ###

  * Download Putty from the following link: [download](http://the.earth.li/~sgtatham/putty/latest/x86/putty.exe).
  * Run Putty by clicking **putty.exe**.
  * On the category, choose **|Connection - SSH - X11|**.  Check **'Enable X11 forwarding'** option.
  * On the category, choose **|Session|**.  Type the followings.
    * Host Name: **verbs.colorado.edu**
    * Port: **22**
  * Click **|Open|** button at the bottom.  If you are connected to Verbs for the first time, a security alert will prompt.  Click **|Yes|** to continue.
  * Enter your ID and password.

## 3a. Run Jubilee ##

  * Goto Jubilee directory
    * cd /home/verbs/shared/propbank/jubilee
  * Run Jubilee
    * ./jubilee.sh

## 3b. Run Jubilee ##
  * Goto Conerstone directory
    * cd /home/verbs/shared/propbank/cornerstone
  * Run Conerstone (LANG = ar | ch | en)
    * ./cornerstone.sh LANG