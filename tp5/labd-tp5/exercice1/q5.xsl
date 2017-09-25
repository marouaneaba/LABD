<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Thu Feb 12 11:41:09 CET 2015 -->

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:xdt="http://www.w3.org/2005/xpath-datatypes"
	xmlns:err="http://www.w3.org/2005/xqt-errors"
	exclude-result-prefixes="xs xdt err fn">

	<xsl:output method="html" indent="yes"/>
	
	<xsl:template match ="/">
		<clubs xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance">
			<xsl:apply-templates select ="//championnat"/>
				
		</clubs>
	</xsl:template>
	
	<xsl:template match="championnat">
		<xsl:for-each select="//club">
		<club>
			<xsl:copy-of select ="./nom"/>
			<xsl:copy-of select ="./ville"/>
			<xsl:variable name="idClub" select="@id"></xsl:variable>
			<recontres>
				<domicile>
					<xsl:for-each select="//rencontre">
						<xsl:if test="./receveur = $idClub">
							<rencotre>
							<xsl:variable name="nom" select="invite"/>
							<club><xsl:value-of select="//club[@id=$nom]/nom"/></club>
							<xsl:copy-of select="./score"/>
							</rencotre>
						</xsl:if>
					</xsl:for-each>
				</domicile>
				
			</recontres>
		</club>
		</xsl:for-each>
	</xsl:template>
	

</xsl:stylesheet>
