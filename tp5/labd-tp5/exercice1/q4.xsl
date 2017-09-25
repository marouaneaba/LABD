<?xml version="1.0" encoding="UTF-8"?>

<!-- New document created with EditiX at Thu Mar 02 12:01:30 CET 2017 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml"/>
	
	<xsl:template match="/">
	<html>
		<body>

		<xsl:apply-templates select="//journees">
			<xsl:with-param name="num" select="18"></xsl:with-param>
		</xsl:apply-templates>/>
		</body>
	</html>
	</xsl:template>
	

	<xsl:template match="journees">
		<xsl:param name="num"></xsl:param>
		
		<xsl:copy-of select="//journee[$num]"/>
	</xsl:template>

</xsl:stylesheet>


