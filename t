
[34mΔ common/rdf/src/main/java/dev/enola/rdf/RdfThingConverter.java[0m
[34m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────[0m

[34m────────────────────────────────────────────────────────────────────────────────────[0m[34m┐[0m
[34m•[0m [34m134[0m:[38;2;248;248;242m [38;2;102;217;239mclass[38;2;248;248;242m [38;2;102;217;239mRdfThingConverter[38;2;248;248;242m [38;2;249;38;114mimplements[38;2;248;248;242m [38;2;166;226;46mConverter[38;2;248;248;242m<[38;2;166;226;46mModel[38;2;248;248;242m, [38;2;166;226;46mStream[38;2;248;248;242m<[38;2;166;226;46mThing[38;2;248;248;242m.[38;2;166;226;46mBuilder[38;2;248;248;242m>> { [0m[34m│[0m
[34m────────────────────────────────────────────────────────────────────────────────────[0m[34m┘[0m
[38;2;248;248;242m            [38;2;249;38;114mif[38;2;248;248;242m ([38;2;166;226;46mCoreDatatype[38;2;248;248;242m.[38;2;190;132;255mXSD[38;2;248;248;242m.[38;2;190;132;255mSTRING[38;2;248;248;242m.getIri().equals(datatype)) {[0m
[38;2;248;248;242m                value.setString(rdfValue.stringValue());[0m

[48;2;63;0;1m            } else if (optLang.isPresent()) {[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                var langString = dev.enola.thing.Value.LangString.newBuilder();[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                langString.setText(rdfLiteral.stringValue());[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                langString.setLang(optLang.get());[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                value.setLangString(langString);[0m[48;2;63;0;1m[0K[0m
[48;2;0;40;0;38;2;248;248;242m            [48;2;0;96;0m    [38;2;117;113;94m// [48;2;0;40;0m} else if (optLang.isPresent()) {[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0mvar langString = dev.enola.thing.Value.LangString.newBuilder();[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0mlangString.setText(rdfLiteral.stringValue());[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0mlangString.setLang(optLang.get());[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0mvalue.setLangString(langString);[0m[48;2;0;40;0m[0K[0m

[38;2;248;248;242m            } [38;2;249;38;114melse[38;2;248;248;242m {[0m
[38;2;248;248;242m                [38;2;102;217;239mvar[38;2;248;248;242m literal [38;2;249;38;114m=[38;2;248;248;242m [38;2;166;226;46mdev[38;2;248;248;242m.[38;2;166;226;46menola[38;2;248;248;242m.[38;2;166;226;46mthing[38;2;248;248;242m.[38;2;166;226;46mValue[38;2;248;248;242m.[38;2;166;226;46mLiteral[38;2;248;248;242m.newBuilder();[0m

[34mΔ common/rdf/src/main/java/dev/enola/rdf/ThingRdfConverter.java[0m
[34m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────[0m

[34m─────────────────────────────────────────────────[0m[34m┐[0m
[34m•[0m [34m27[0m:[38;2;248;248;242m [38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46mdev[38;2;248;248;242m.[38;2;166;226;46menola[38;2;248;248;242m.[38;2;166;226;46mcommon[38;2;248;248;242m.[38;2;166;226;46mconvert[38;2;248;248;242m.[38;2;166;226;46mConverter[38;2;248;248;242m; [0m[34m│[0m
[34m─────────────────────────────────────────────────[0m[34m┘[0m
[38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46mdev[38;2;248;248;242m.[38;2;166;226;46menola[38;2;248;248;242m.[38;2;166;226;46mcommon[38;2;248;248;242m.[38;2;166;226;46mconvert[38;2;248;248;242m.[38;2;166;226;46mConverterInto[38;2;248;248;242m;[0m
[38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46mdev[38;2;248;248;242m.[38;2;166;226;46menola[38;2;248;248;242m.[38;2;166;226;46mthing[38;2;248;248;242m.[38;2;166;226;46mThing[38;2;248;248;242m;[0m
[38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46mdev[38;2;248;248;242m.[38;2;166;226;46menola[38;2;248;248;242m.[38;2;166;226;46mthing[38;2;248;248;242m.[38;2;166;226;46mThingOrBuilder[38;2;248;248;242m;[0m
[48;2;63;0;1mimport dev.enola.thing.Value.LangString;[0m[48;2;63;0;1m[0K[0m

[38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46morg[38;2;248;248;242m.[38;2;166;226;46meclipse[38;2;248;248;242m.[38;2;166;226;46mrdf4j[38;2;248;248;242m.[38;2;166;226;46mmodel[38;2;248;248;242m.[38;2;166;226;46mBNode[38;2;248;248;242m;[0m
[38;2;249;38;114mimport[38;2;248;248;242m [38;2;166;226;46morg[38;2;248;248;242m.[38;2;166;226;46meclipse[38;2;248;248;242m.[38;2;166;226;46mrdf4j[38;2;248;248;242m.[38;2;166;226;46mmodel[38;2;248;248;242m.[38;2;166;226;46mIRI[38;2;248;248;242m;[0m

[34m───────────────────────────────[0m[34m┐[0m
[34m•[0m [34m117[0m:[38;2;248;248;242m [38;2;102;217;239mclass[38;2;248;248;242m [38;2;102;217;239mThingRdfConverter[38;2;248;248;242m [0m[34m│[0m
[34m───────────────────────────────[0m[34m┘[0m
[38;2;248;248;242m                        vf.createLiteral(literal.getValue(), createIRI(literal.getDatatype())));[0m
[38;2;248;248;242m            [38;2;248;248;240m}[0m

[48;2;63;0;1m            case LANG_STRING -> {[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                LangString langString = value.getLangString();[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m                yield singleton(vf.createLiteral(langString.getText(),[48;2;144;16;17m langString.getLang()));[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m            }[0m[48;2;63;0;1m[0K[0m
[48;2;0;40;0;38;2;248;248;242m            [48;2;0;96;0m    [38;2;117;113;94m// [48;2;0;40;0mcase LANG_STRING -> {[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0mLangString langString = value.getLangString();[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [48;2;0;96;0;38;2;117;113;94m//     [48;2;0;40;0myield singleton(vf.createLiteral(langString.getText(),[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m                [38;2;117;113;94m// langString.getLang()));[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m            [48;2;0;96;0m    [38;2;117;113;94m// [48;2;0;40;0m}[0m[48;2;0;40;0m[0K[0m

[38;2;248;248;242m            [38;2;249;38;114mcase[38;2;248;248;242m [38;2;253;151;31mSTRUCT[38;2;248;248;242m [38;2;102;217;239m->[38;2;248;248;242m {[0m
[38;2;248;248;242m                [38;2;166;226;46mBNode[38;2;248;248;242m bNode;[0m

[34mΔ common/rdf/src/test/resources/picasso.thing.yaml[0m
[34m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────[0m

[34m──────[0m[34m┐[0m
[34m•[0m [34m16[0m: [34m│[0m
[34m──────[0m[34m┘[0m

[38;2;249;38;114miri[38;2;248;248;242m: [38;2;230;219;116mhttp://example.enola.dev/Picasso[0m
[38;2;249;38;114mfields[38;2;248;248;242m:[0m
[48;2;63;0;1m  http://www.w3.org/ns/locn#location: { lang_string: { text: "Spain", lang: en } }[0m[48;2;63;0;1m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [38;2;249;38;114mhttp://www.w3.org/ns/locn#location[38;2;248;248;242m: { [48;2;0;96;0;38;2;249;38;114mstring[38;2;248;248;242m: [38;2;230;219;116m"Spain"[38;2;248;248;242m } [38;2;117;113;94m# { [48;2;0;40;0mlang_string: { text: "Spain", lang: en } }[0m[48;2;0;40;0m[0K[0m
[38;2;248;248;242m  [38;2;117;113;94m# TODO http://www.w3.org/ns/locn#location: { lang_string: { text: "España", lang: es } }[0m
[38;2;248;248;242m  [38;2;249;38;114mhttp://xmlns.com/foaf/0.1/firstName[38;2;248;248;242m: { [38;2;249;38;114mstring[38;2;248;248;242m: [38;2;230;219;116m"Pablo"[38;2;248;248;242m }[0m
[38;2;248;248;242m  [38;2;249;38;114mhttp://www.w3.org/1999/02/22-rdf-syntax-ns#type[38;2;248;248;242m: { [38;2;249;38;114mlink[38;2;248;248;242m: { [38;2;249;38;114miri[38;2;248;248;242m: [38;2;230;219;116m"http://example.enola.dev/Artist"[38;2;248;248;242m } }[0m

[34mΔ common/thing/thing.proto[0m
[34m────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────[0m

[34m──────────────────────[0m[34m┐[0m
[34m•[0m [34m54[0m:[38;2;248;248;242m [38;2;102;217;239mmessage[38;2;248;248;242m [38;2;102;217;239mValue[38;2;248;248;242m { [0m[34m│[0m
[34m──────────────────────[0m[34m┘[0m

[38;2;248;248;242m    [38;2;117;113;94m// TODO Keep or remove this? See[0m
[38;2;248;248;242m    [38;2;117;113;94m// https://github.com/enola-dev/enola/pull/540...[0m
[48;2;63;0;1m    LangString lang_string = 4;[0m[48;2;63;0;1m[0K[0m
[48;2;0;40;0;38;2;248;248;242m    [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0mLangString lang_string = 4;[0m[48;2;0;40;0m[0K[0m

[38;2;248;248;242m    [38;2;117;113;94m// Sub-structure (contained) Thing.[0m
[38;2;248;248;242m    Thing struct = 18;[0m

[34m───────────────────────[0m[34m┐[0m
[34m•[0m [34m105[0m:[38;2;248;248;242m [38;2;102;217;239mmessage[38;2;248;248;242m [38;2;102;217;239mValue[38;2;248;248;242m { [0m[34m│[0m
[34m───────────────────────[0m[34m┘[0m

[38;2;248;248;242m  [38;2;117;113;94m// TODO Keep or remove this? See[0m
[38;2;248;248;242m  [38;2;117;113;94m// https://github.com/enola-dev/enola/pull/540...[0m
[48;2;63;0;1m  message LangString {[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m    // Text, for humans.[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m    string text = 1;[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m[0K[0m
[48;2;63;0;1m    // BCP 47 “language tag” (e.g. “de-ch”) of the text.[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m    string lang = 2;[0m[48;2;63;0;1m[0K[0m
[48;2;63;0;1m  }[0m[48;2;63;0;1m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0mmessage LangString {[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0m  // Text, for humans.[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0m  string text = 1;[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [38;2;117;113;94m//[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0m  // BCP 47 “language tag” (e.g. “de-ch”) of the text.[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0m  string lang = 2;[0m[48;2;0;40;0m[0K[0m
[48;2;0;40;0;38;2;248;248;242m  [48;2;0;96;0;38;2;117;113;94m// [48;2;0;40;0m}[0m[48;2;0;40;0m[0K[0m

[38;2;248;248;242m  [38;2;102;217;239mmessage[38;2;248;248;242m [38;2;102;217;239mList[38;2;248;248;242m {[0m
[38;2;248;248;242m    [38;2;249;38;114mrepeated[38;2;248;248;242m [38;2;102;217;239mValue[38;2;248;248;242m [38;2;255;255;255mvalues[38;2;248;248;242m [38;2;249;38;114m=[38;2;248;248;242m [38;2;190;132;255m1[38;2;248;248;242m;[0m