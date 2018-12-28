# Object Detection for Autopsy
This module is developed in [Java](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html), allow to use a state-of-the-art, real-time object detection system called [Yolo](https://pjreddie.com/darknet/yolo/) on open-source forensics software called [Autopsy](https://www.autopsy.com).
<br>This module is a FileIngestModule, in the future will bring a ReportModule too.

It is sponsored by [Instituto de Telecomunicações](https://www.it.pt) developed by [Rúben Caceiro](https://github.com/rcaceiro) and guided by [Patrício Domingues](https://scholar.google.com/citations?user=LPwSQ2EAAAAJ&hl=en).

## Mode of operation
### Remote mode
This mode has two components:
1) [object.detection4autopsy.server](https://github.com/rcaceiro/object.detection4autopsy.server), the [installation process](https://github.com/rcaceiro/object.detection4autopsy.server#installation) is described on repository.
2) [object.detection4autopsy](https://github.com/rcaceiro/obejct.detection4autopsy), the installation process is described [below](https://github.com/rcaceiro/object.detection4autopsy#installation)

**Pros:**
- Allow to invest on a powerfull machine and share between several users, insteaded invest in several machines less powerfull.
- Allow to not block the computer to another tasks, because this process requires a lot resources and can leave the machine less usable.
- You can use this mode to simulate the local mode you have to install the server on you machine and configure the module to connect to 127.0.0.1 or localhost and the behaviour is local. **(If you want use this locally must use this setup for now)**

**Cons:**
- This mode of operation can be a bottle neck, because if a task took to long to finish the rest will be delayed.

### Local mode
**Not implemented yet**
This mode only has on component that is this module, and allow to the user take functionality locally.

**Pros:**
- This mode of operation will not be a bottle neck, because only process the local ingest
- Allow to you extract a first glance of your results.

**Cons:**
- You have to invest in a better machine, a common worksation, with 2 or 4 cores and no nVidia GPU, cannot extract the results as fast as you want.
- You may notice that your machine became slow and a little unresponsive while run the insgest module

## Minimum Requirements
- Autopsy 4.7.0 or newer
- Windows, Linux or macOS

## Instalation
- Download the lastest [release](https://github.com/rcaceiro/obejct.detection4autopsy/releases) nbm file.
- Click Tools
 ![Main page](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/1.PNG?raw=true)
- Click Plugins
 ![After tools clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/2.png?raw=true)
- Click Downloaded tab and then Add Plugins button
 ![After plugins clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/3.png?raw=true)
- Find the file and then click open
 ![After add plugins clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/4.png?raw=true)
- Click Install
 ![After open clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/5.png?raw=true)
- Click Next
 ![After install clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/6.png?raw=true)
- Accept the terms and then Next
 ![After next clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/7.png?raw=true)
- Click Continue
 ![After continue clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/8.png?raw=true)
- Click Close
 ![After continue clicked](https://github.com/rcaceiro/obejct.detection4autopsy/blob/master/installation_tuturial/9.png?raw=true)
- **Will prompt that module signature isn't trustable, we try fix this problem**
- And now you can run the ingest modules has usual
- **After this if you want to use remote you must have to [install the server](https://github.com/rcaceiro/object.detection4autopsy.server#installation) and run it**
