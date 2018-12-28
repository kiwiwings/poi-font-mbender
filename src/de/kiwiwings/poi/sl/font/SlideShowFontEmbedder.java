/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package de.kiwiwings.poi.sl.font;

import de.kiwiwings.sfntly.Font;
import de.kiwiwings.sfntly.FontFactory;
import de.kiwiwings.sfntly.Tag;
import de.kiwiwings.sfntly.eot.EOTWriter;
import de.kiwiwings.sfntly.subsetter.RenumberingSubsetter;
import de.kiwiwings.sfntly.subsetter.Subsetter;
import de.kiwiwings.sfntly.table.core.CMap;
import de.kiwiwings.sfntly.table.core.CMapTable;
import de.kiwiwings.sfntly.table.core.NameTable;
import de.kiwiwings.sfntly.table.core.OS2Table;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.extractor.SlideShowExtractor;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.TextBox;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class SlideShowFontEmbedder {
    private final SlideShow slideShow;

    // generate MicroTypeExpress (mtx) fonts
    private final EOTWriter conv = new EOTWriter(true);

    private final FontFactory fontfac = FontFactory.getInstance();

    public SlideShowFontEmbedder(SlideShow slideShow) {
        this.slideShow = slideShow;
    }

    public void embed(File fontFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(fontFile)) {
            embed(fis);
        }
    }

    public void embed(InputStream fontData) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(200000);

        Font[] fonts = fontfac.loadFonts(fontData);
        for (Font font : fonts) {
            Font subFont = subsetFont(font, getCodepoints(font));

            bos.reset();
            conv.convert(subFont).copyTo(bos);

            slideShow.addFont(new ByteArrayInputStream(bos.toByteArray()));
        }
    }

    private BitSet getCodepoints(Font font) throws IOException {
        try (SlideShowExtractor sse = new SlideShowExtractor(slideShow)) {
            sse.setFilesystem(null);
            sse.setMasterByDefault(true);

            OS2Table os2 = font.getTable(Tag.OS_2);
            boolean italic = false, bold = false;
            if (os2 != null) {
                final EnumSet<OS2Table.FsSelection> sel = os2.fsSelection();
                italic = sel.contains(OS2Table.FsSelection.ITALIC) || sel.contains(OS2Table.FsSelection.OBLIQUE);
                bold = os2.usWeightClass() > 400;
            }

            NameTable names = font.getTable(Tag.name);
            String typeface = names.name(NameTable.NameId.FontFamilyName.value());

            // for regular styled fonts, check all styles (italic and/or bold too)
            return sse.getCodepoints(typeface, italic ? true : null, bold ? true : null);
        }
    }

    private Font subsetFont(Font font, BitSet codepoints) throws IOException {
        if (codepoints.isEmpty()) {
            // font hasn't been used yet -> import full font, as we don't know which glyphs will be used
            return font;
        }

        Subsetter ss = new RenumberingSubsetter(font, fontfac);
        CMapTable cm = font.getTable(Tag.cmap);

        // this assumes, that the font file has a Unicode CMap
        // If only a Mac or Windows encoding is available, we would need to map the codepoints
        // first to the corresponding charsets and then determine the correct glyphs
        CMap cmap = cm.cmap(Font.PlatformId.Unicode.value());
        List<Integer> glyphIds = codepoints.stream().mapToObj(cmap::glyphId).collect(Collectors.toList());

        ss.setGlyphs(glyphIds);
        return ss.subset().build();
    }

    public static void main(String[] args) throws Exception {
        SlideShow[] ppts = { new XMLSlideShow(), new HSLFSlideShow() };

        for (SlideShow ppt : ppts) {
            Slide slide =  ppt.createSlide();

            TextBox tb2 = slide.createTextBox();
            tb2.setAnchor(new Rectangle2D.Double(100, 100, 100, 50));
            tb2.setText("arial test").setFontFamily("Arial");

            TextBox tb3 = slide.createTextBox();
            tb3.setAnchor(new Rectangle2D.Double(100, 200, 100, 50));
            tb3.setText("calibri test").setFontFamily("Calibri");

            TextBox tb1 = slide.createTextBox();
            tb1.setAnchor(new Rectangle2D.Double(100, 300, 100, 50));
            tb1.setText("font test").setFontFamily("Droid Sans");



            SlideShowFontEmbedder emb = new SlideShowFontEmbedder(ppt);

            emb.embed(new File("data/testdata/DroidSans-Regular.ttf"));

            String fileExt = (ppt instanceof XMLSlideShow) ? ".pptx" : ".ppt";

            try (FileOutputStream fos = new FileOutputStream("fonttest" + fileExt)) {
                ppt.write(fos);
            }
        }
    }
}
