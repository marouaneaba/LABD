<?php
  $local_panier="panier_local.xml";

  $xml_file=fopen($local_panier,"w");
  fprintf($xml_file,"<BASKET/>");
  fclose($xml_file);

  passthru($cmd);
  echo "<meta http-equiv='refresh' content='0; panier.php' />";

  ?>
