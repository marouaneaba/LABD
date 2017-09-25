<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:include href="html_wrapper.xsl"/>

<xsl:template match="/">
  <xsl:variable name='catalog_xml' select="//param[@name='catalog_xml']"/>
  <xsl:variable name='families_xml' select="//param[@name='families_xml']"/>
  <xsl:variable name='family' select="//param[@name='family']"/>
  <xsl:variable name="sort_key" select="//param[@name='sort_key']"/>
  <!-- attention, il ne sait pas gerer la transmission des arbres XML -->

  <html>
    <xsl:call-template name="header"/>
    <body>
      <xsl:call-template name="menu"/>
      <div id="content">
          <xsl:apply-templates select="document($families_xml)/CLASSIFICATION"
            mode="generate-select">
              <xsl:with-param name='default' select="$family"/>
          </xsl:apply-templates>
	
	<xsl:variable name ="species" select ="document($families_xml)/CLASSIFICATION/FAMILY[NAME/text() = 
	$family]/SPECIES"/>
	
          <xsl:apply-templates select="document($catalog_xml)//CATALOG">
            <xsl:with-param name="sort_key" select="$sort_key"/>
            <xsl:with-param name="family" select="$family"/>
            <xsl:with-param name="families_xml" select="$families_xml"/>
			<xsl:with-param name="species" slect ="$species"/>
		  </xsl:apply-templates>
      </div>
    </body>
  </html>

</xsl:template>

<xsl:template match="CLASSIFICATION" mode="generate-select">
  <xsl:param name="default"/>
  <form>
    <b>Filtrer par Famille</b> : <select name="family" onChange="javascript:submit()">
      
	  <option value="">Aucune</option>
      <xsl:apply-templates select="FAMILY" mode="generate-select">
        <xsl:with-param name="default" select="$default"/>
      </xsl:apply-templates>
    </select>
  </form>
</xsl:template>

<xsl:template match="FAMILY" mode="generate-select">
  <xsl:param name="default"/>
  
  <xsl:variable name ="species" select ="NAME[following-sibling::species = default]"/>
	
</xsl:template>

<xsl:template match="CATALOG">
  <xsl:param name="sort_key"/>
  <xsl:param name="family"/>
  <xsl:param name="families_xml"/>
  <xsl:param name "species"/>
  
  <xsl:apply-templates select="PLANT" mode="generate-select">
        <xsl:with-param name="default" select="$family"/>
		<xsl:with-param name ="species" select ="$species"/>
  </xsl:apply-templates>
  
</xsl:template>

<xsl:template match="PLANT">
  <xsl:param  name ="family"/>
  
  
  <xsl:for-each select =".">
	<xsl:if test ="*[name() == $family">
			<xsl:value-of  name ="*"/>
	</xsl:if>
  </xsl:for-each>
  
</xsl:template>

</xsl:stylesheet>
