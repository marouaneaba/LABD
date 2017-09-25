<?xml version='1.0' encoding='UTF-8'?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns="http://labd/art"
	xpath-default-namespace="http://labd/art">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
	<artistes>
	  <xsl:for-each select="//artiste">
		<artiste>
		  <xsl:variable name="nomArtiste" select="./nom"/>
		  <nom><xsl:value-of select="./nom"/></nom>
		  <nbOeuvres><xsl:value-of select="count(document('catalogue-1.xml')//oeuvre/auteur/nom[.='Picasso'])"/></nbOeuvres>
		</artiste>
	   </xsl:for-each>
	</artistes>
  </xsl:template>

</xsl:stylesheet>
