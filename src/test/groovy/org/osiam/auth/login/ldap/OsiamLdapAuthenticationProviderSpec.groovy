/**
 * The MIT License (MIT)
 *
 * Copyright (C) 2013-2016 tarent solutions GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.osiam.auth.login.ldap

import org.osiam.auth.login.internal.InternalAuthentication
import org.osiam.resources.provisioning.SCIMUserProvisioning
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.ldap.authentication.LdapAuthenticator
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator
import spock.lang.Specification

class OsiamLdapAuthenticationProviderSpec extends Specification {

    DefaultLdapAuthoritiesPopulator rolePopulator = Mock()
    LdapAuthenticator authenticator = Mock()
    OsiamLdapUserContextMapper mapper = Mock()
    SCIMUserProvisioning provisioning = Mock()
    OsiamLdapAuthenticationProvider provider = new OsiamLdapAuthenticationProvider(authenticator, rolePopulator,
            mapper, provisioning, true)

    def 'the LDAP provider only supports OsiamLdapAuthentication class'() {
        expect:
        provider.supports(OsiamLdapAuthentication)
        !provider.supports(InternalAuthentication)
        !provider.supports(UsernamePasswordAuthenticationToken)
    }
}
