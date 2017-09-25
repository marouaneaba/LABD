<?xml version='1.0' encoding='UTF-8'?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
	<clubs>
	<xsl:apply-templates select="championnat"/>
	</clubs>
  </xsl:template>

  <xsl:template match="championnat">
	<xsl:for-each select="//club">
		<club>
		<nom><xsl:value-of select="nom"/></nom>
		<ville><xsl:value-of select="ville"/></ville>
		<xsl:variable name="club" select="@id"/>
		<rencontres>
			<domicile>
				<xsl:for-each select="//rencontre">
					<xsl:if test="./receveur=$club">
						<rencontre>
							<xsl:variable name="nom" select="invite"/>
							<club><xsl:value-of select="//club[@id=$nom]/nom"/></club>
							<score><xsl:value-of select="score"/></score>
						</rencontre>
					</xsl:if>
				</xsl:for-each>
			</domicile>
			<exterieur>
				<xsl:for-each select="//rencontre">
					<xsl:if test="./invite=$club">
						<rencontre>
							<xsl:variable name="nom" select="receveur"/>
							<club><xsl:value-of select="//club[@id=$nom]/nom"/></club>
							<score><xsl:value-of select="score"/></score>
						</rencontre>
					</xsl:if>
				</xsl:for-each>
			</exterieur>
		</rencontres>
		</club>
	</xsl:for-each>
  </xsl:template>
  
</xsl:stylesheet>
