<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Wed Mar 08 06:06:38 CET 2017 -->

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://labd/art"
	xpath-default-namespace="http://labd/art">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
		
<artistes>
		
		<xsl:apply-templates select="//artistes"/>
		</artistes>
	</xsl:template>
	
	<xsl:template match="artistes">
		
			<xsl:for-each select="./artiste">
			<artiste>
				<nom><xsl:value-of select="./nom"/></nom>
				<prenom><xsl:value-of select="./prénom"/></prenom>
				<xsl:variable name="name" select="./nom"/>
				<xsl:variable name="prenom" select="./prénom"/>
				<nb-oeuvre><xsl:value-of select="count(document('cataloque-1.xml')//oeuvre[./auteur/nom =$name and ./auteur/prénom = $prenom])"/>
				</nb-oeuvre>
			</artiste>
			</xsl:for-each>
		
	</xsl:template>

</xsl:stylesheet>
