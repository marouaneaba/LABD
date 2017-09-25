<?php  header("Content-type: text/xml");?>
<?xml-stylesheet type="text/xsl" href="catalogue_trie.xsl"?>
<?php
  echo "<page>
          <params>
            <param name='cle_tri'>".$_REQUEST['cle_tri']."</param>
            <param name='catalog_xml'>plant_catalog.xml</param>
          </params>
        </page>";
  ?>
