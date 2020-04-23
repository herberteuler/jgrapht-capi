#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#include <jgrapht_capi.h>

#define ILLEGAL_ARGUMENT 2

int main() {
    graal_isolate_t *isolate = NULL;
    graal_isolatethread_t *thread = NULL;

    if (thread, graal_create_isolate(NULL, &isolate, &thread) != 0) {
        fprintf(stderr, "graal_create_isolate error\n");
        exit(EXIT_FAILURE);
    }

    void * set;
    jgrapht_capi_set_linked_create(thread, &set);

    int exists;
    jgrapht_capi_set_long_contains(thread, set, 4, &exists);
    assert(exists == 0);

    jgrapht_capi_set_long_add(thread, set, 4);
    jgrapht_capi_set_long_contains(thread, set, 4, &exists);
    assert(exists == 1);

    jgrapht_capi_set_long_add(thread, set, 100);
    jgrapht_capi_set_long_contains(thread, set, 100, &exists);
    assert(exists == 1);

    jgrapht_capi_set_long_add(thread, set, 500);
    jgrapht_capi_set_long_contains(thread, set, 500, &exists);
    assert(exists == 1);

    long long size;
    jgrapht_capi_set_size(thread, set, &size);
    assert(size == 3);

    jgrapht_capi_set_long_remove(thread, set, 500);
    jgrapht_capi_set_long_contains(thread, set, 500, &exists);
    assert(exists == 0);

    jgrapht_capi_set_size(thread, set, &size);
    assert(size == 2);

    void * it;
    long long elem;
    jgrapht_capi_set_it_create(thread, set, &it);
    jgrapht_capi_it_next_long(thread, it, &elem);
    assert(elem == 4);
    jgrapht_capi_it_next_long(thread, it, &elem);
    assert(elem == 100);
    int hasnext;
    jgrapht_capi_it_hasnext(thread, it, &hasnext);
    assert(hasnext == 0);
    jgrapht_capi_destroy(thread, it);

    jgrapht_capi_set_clear(thread, set);
    jgrapht_capi_set_size(thread, set, &size);
    assert(size == 0);

    jgrapht_capi_destroy(thread, set);
    assert(jgrapht_capi_get_errno(thread) == 0);
   
    if (thread, graal_detach_thread(thread) != 0) {
        fprintf(stderr, "graal_detach_thread error\n");
        exit(EXIT_FAILURE);
    }

    return EXIT_SUCCESS;
}