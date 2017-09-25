<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:include href="html_wrapper.xsl"/>

  <xsl:template match="/">
    <html>
      <xsl:call-template name="header"/>
      <body>
        <xsl:call-template name="menu"/>
        <div id="content">
            <b>Bienvenue !</b>
        </div>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
