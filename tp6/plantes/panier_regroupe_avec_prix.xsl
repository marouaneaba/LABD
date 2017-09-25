<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="html_wrapper.xsl"/>

<xsl:template match="/">
  <xsl:variable name='catalog_xml' select="//param[@name='catalog_xml']"/>
  <xsl:variable name='panier_xml' select="//param[@name='panier_xml']"/>
  <html>
    <xsl:call-template name="header"/>
    <body>
      <xsl:call-template name="menu"/>
      <div id="content">
          <xsl:apply-templates select="document($panier_xml)/BASKET">
            <xsl:with-param name="catalog_xml" select="$catalog_xml"/>
          </xsl:apply-templates>
      </div>
    </body>
  </html>

</xsl:template>

<xsl:template match="BASKET">
  <xsl:param name="catalog_xml"/>

  Templates BASKET, PLANT a implementer.

</xsl:template>


</xsl:stylesheet>
