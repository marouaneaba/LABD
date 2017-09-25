<?xml version="1.0" encoding="UTF-8"?>

<!-- New document created with EditiX at Thu Mar 02 11:12:28 CET 2017 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml"/>
	
	<xsl:template match="/">
	<html>
		<body>
		<xsl:apply-templates  select="/championnat/clubs"/>
		</body>
	</html>
	</xsl:template>

	

	<xsl:template match="nom">
    	<p>nom : <xsl:value-of select="."></xsl:value-of></p>
	</xsl:template>
		
	<xsl:template match="ville">
	    <p>ville : <xsl:value-of select="."></xsl:value-of></p>
	</xsl:template>

</xsl:stylesheet>


