# poi-font-mbender

This example project faciliates [sfntly][1] to convert true type fonts to [EOT/MTX][2]
for embedding into [Apache POI][3] slideshow.
As sfntly wasn't available as maven artifacts, it was necessary to fork the sources.
The sfntly package is basically just repackaged.

For POIs usage, the interesting class is [SlideShowFontEmbedder][4]

The typo in the name is based on the character [Bender][5]
while watching Futurama in a marathon sessions with my kids ...  

# See also

* https://github.com/rillig/sfntly

[1]: https://opensource.googleblog.com/2011/11/build-great-font-tools-and-services.html
[2]: https://www.w3.org/Submission/EOT
[3]: https://poi.apache.org
[4]: src/de/kiwiwings/poi/sl/font/SlideShowFontEmbedder.java
[5]: https://en.wikipedia.org/wiki/Bender_(Futurama)