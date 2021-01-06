package com.okay.service;

import org.springframework.stereotype.Component;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;

@Component
public class LdapServiceImpl implements LdapService {

    @Override
    public void login(String username, String password) {

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "Simple");
        env.put(Context.PROVIDER_URL, "ldap://localhost:389");
//        env.put(Context.SECURITY_PRINCIPAL, "uid=admin,dc=ahmetokay"); // replace with user DN
        env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=ahmetokay"); // replace with user DN
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            DirContext context = new InitialDirContext(env);

            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String[] attrIDs = { "distinguishedName", "sn", "givenname", "mail", "telephonenumber", "memberOf","image", "CN" };
            constraints.setReturningAttributes(attrIDs);

            NamingEnumeration<?> answer = context.search("DC=ahmetokay","port_number=389", constraints);
//            NamingEnumeration<?> answer = context.search("DC=ahmetokay","sAMAccountName=" + username, constraints);
            if (answer.hasMore()) {
                Attributes attrs = ((SearchResult) answer.next()).getAttributes();
                System.out.println("image " + attrs.get("image"));
                System.out.println("distinguishedName " + attrs.get("distinguishedName"));
                System.out.println("givenname " + attrs.get("givenname"));
                System.out.println("sn " + attrs.get("sn"));
                System.out.println("mail " + attrs.get("mail"));
                System.out.println("telephonenumber " + attrs.get("telephonenumber"));
                System.out.println("memberof " + attrs.get("memberof"));

            } else {
                throw new Exception("Invalid User");
            }
        } catch (NameNotFoundException e) {
            System.out.println("hata");
            System.out.println(e.getMessage());
            // handle
        } catch (AuthenticationException e) {
            // hatalı kullanıcı adı şifre
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            // close ctx or do Java 7 try-with-resources http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        }
    }
}
