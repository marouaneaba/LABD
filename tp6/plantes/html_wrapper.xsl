<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template name="menu">
      <div id="menu" style="float:left; padding-right:20px;">
        <ul>
          <li>
            Catalogue
            <ul>
              <li><a href="catalogue_trie.php">plantes</a></li>
              <li><a href="catalogue_avec_familles.php">plantes et familles</a></li>
              <li><a href="catalogue_par_famille.php">plantes par famille</a></li>
            </ul>
          </li>
          <li>
            Panier
            <ul>
              <li>unitaire : <a href="panier.php">sans prix</a> / <a href="panier_avec_prix.php">avec prix</a></li>
              <li>par plantes : <a href="panier_regroupe.php">sans prix</a> / <a href="panier_regroupe_avec_prix.php">avec prix</a></li>
              <li><a href="panier_vider.php">vider panier</a></li>
            </ul>
          </li>
          <li>
            Ventes
            <ul>
              <li>Liste</li>
              <li>Total</li>
            </ul>
          </li>
        </ul>
      </div>
  </xsl:template>
  <xsl:template name="header">
      <head>
        <title>Catalogue de plantes</title>
        <style>
        table,tr,td {
          border-width:2px 2px 2px 2px;
          border-style:solid;
          border-collapse:collapse;
        }
        </style>
      </head>
  </xsl:template>
</xsl:stylesheet>
