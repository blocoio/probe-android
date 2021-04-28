package org.openobservatory.ooniprobe;

import org.openobservatory.ooniprobe.model.database.Network;

import io.bloco.faker.Faker;

public class NetworkFactory {

    private static final Faker faker = new Faker();

    public static Network build() {
        Network temp = new Network();

        temp.id = faker.number.positive();
        temp.asn = faker.internet.domainWord();
        temp.ip = faker.internet.ipV4Address();
        temp.network_name = faker.internet.domainName();
        temp.network_type = faker.internet.domainSuffix();
        temp.country_code =  faker.address.countryCode();

        return temp;
    }
}
