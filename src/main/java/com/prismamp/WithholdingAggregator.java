package com.prismamp;

import com.prismamp.model.Certificate;
import com.prismamp.model.CertificateItem;
import com.prismamp.model.Tax;
import java.util.Map;
import org.apache.kafka.streams.kstream.Aggregator;

public class WithholdingAggregator implements Aggregator<TaxGroupingKey, Tax, Certificate> {

  @Override
  public Certificate apply(
      final TaxGroupingKey key, final Tax taxEvent, final Certificate certificate) {

    certificate.setCuit(taxEvent.getCuit());
    certificate.setTaxId(taxEvent.getTaxId());
    Map<Double, CertificateItem> taxRates = certificate.getTaxRates();
    CertificateItem item =
        taxRates.computeIfAbsent(taxEvent.getTaxRate(), k -> new CertificateItem(k));
    item.addToTotalBaseTax(taxEvent.getBaseTax());
    item.addToTotalTaxAmount(taxEvent.getTaxAmount());

    return certificate;
  }
}
